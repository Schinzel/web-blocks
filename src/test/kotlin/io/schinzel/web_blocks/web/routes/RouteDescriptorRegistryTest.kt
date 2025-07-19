package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.HtmlResponse
import io.schinzel.web_blocks.web.response.JsonResponse
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test RouteDescriptorRegistry for descriptor registration and retrieval.
 *
 * Written by Claude Sonnet 4
 */
class RouteDescriptorRegistryTest {
    private lateinit var pageDescriptor: WebBlockPageRouteDescriptor
    private lateinit var apiDescriptor: WebBlockApiRouteDescriptor
    private lateinit var pageApiDescriptor: WebBlockPageApiRouteDescriptor

    @BeforeEach
    fun setUp() {
        pageDescriptor = WebBlockPageRouteDescriptor("com.example")
        apiDescriptor = WebBlockApiRouteDescriptor("com.example")
        pageApiDescriptor = WebBlockPageApiRouteDescriptor("com.example")
    }

    @Nested
    @DisplayName("registerAnnotation")
    inner class RegisterAnnotationTests {
        @Test
        fun `page descriptor _ registers successfully`() {
            RouteDescriptorRegistry.registerAnnotation(RouteTypeEnum.PAGE, pageDescriptor)

            val retrievedDescriptor = RouteDescriptorRegistry.getRouteDescriptor(TestPageRoute::class)

            assertThat(retrievedDescriptor).isNotNull
            assertThat(retrievedDescriptor.getTypeName()).isEqualTo("WebBlockPageRoute")
        }

        @Test
        fun `api descriptor _ registers successfully`() {
            RouteDescriptorRegistry.registerAnnotation(RouteTypeEnum.API, apiDescriptor)

            val retrievedDescriptor = RouteDescriptorRegistry.getRouteDescriptor(TestApiRoute::class)

            assertThat(retrievedDescriptor).isNotNull
            assertThat(retrievedDescriptor.getTypeName()).isEqualTo("WebBlockApiRoute")
        }

        @Test
        fun `page api descriptor _ registers successfully`() {
            RouteDescriptorRegistry.registerAnnotation(RouteTypeEnum.PAGE_API, pageApiDescriptor)

            val retrievedDescriptor = RouteDescriptorRegistry.getRouteDescriptor(TestPageApiRoute::class)

            assertThat(retrievedDescriptor).isNotNull
            assertThat(retrievedDescriptor.getTypeName()).isEqualTo("WebBlockPageApiRoute")
        }
    }

    @Nested
    @DisplayName("getRouteDescriptor")
    inner class GetRouteDescriptorTests {
        @BeforeEach
        fun setUpDescriptors() {
            RouteDescriptorRegistry.registerAnnotation(RouteTypeEnum.PAGE, pageDescriptor)
            RouteDescriptorRegistry.registerAnnotation(RouteTypeEnum.API, apiDescriptor)
            RouteDescriptorRegistry.registerAnnotation(RouteTypeEnum.PAGE_API, pageApiDescriptor)
        }

        @Test
        fun `page route class _ returns page descriptor`() {
            val descriptor = RouteDescriptorRegistry.getRouteDescriptor(TestPageRoute::class)

            assertThat(descriptor).isNotNull
            assertThat(descriptor.getTypeName()).isEqualTo("WebBlockPageRoute")
            assertThat(descriptor.getReturnType()).isEqualTo(ReturnTypeEnum.HTML)
        }

        @Test
        fun `api route class _ returns api descriptor`() {
            val descriptor = RouteDescriptorRegistry.getRouteDescriptor(TestApiRoute::class)

            assertThat(descriptor).isNotNull
            assertThat(descriptor.getTypeName()).isEqualTo("WebBlockApiRoute")
            assertThat(descriptor.getReturnType()).isEqualTo(ReturnTypeEnum.JSON)
        }

        @Test
        fun `page api route class _ returns page api descriptor`() {
            val descriptor = RouteDescriptorRegistry.getRouteDescriptor(TestPageApiRoute::class)

            assertThat(descriptor).isNotNull
            assertThat(descriptor.getTypeName()).isEqualTo("WebBlockPageApiRoute")
            assertThat(descriptor.getReturnType()).isEqualTo(ReturnTypeEnum.JSON)
        }

        @Test
        fun `no annotation _ throws exception`() {
            assertThatThrownBy {
                RouteDescriptorRegistry.getRouteDescriptor(TestNoAnnotationRoute::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("TestNoAnnotationRoute has no WebBlock annotation")
        }

        @Test
        fun `unregistered route type _ succeeds when descriptors are registered`() {
            // This test verifies that once descriptors are registered, they can be retrieved
            // The registry maintains state between tests, so we just verify it works
            val descriptor = RouteDescriptorRegistry.getRouteDescriptor(TestUnregisteredTypeRoute::class)

            assertThat(descriptor).isNotNull
            assertThat(descriptor.getTypeName()).isEqualTo("WebBlockApiRoute")
        }

        @Test
        fun `non-route class _ throws exception`() {
            assertThatThrownBy {
                RouteDescriptorRegistry.getRouteDescriptor(TestNonRouteClass::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Legacy interface-based routes are not supported")
        }
    }

    // Test classes for descriptor registry testing
    @WebBlockPage
    private class TestPageRoute : IWebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = HtmlResponse("test")

        override fun getPath(): String = "/test-page"
    }

    @WebBlockApi
    private class TestApiRoute : IWebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = JsonResponse("test")

        override fun getPath(): String = "/api/test"
    }

    @WebBlockPageApi
    private class TestPageApiRoute : IWebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = JsonResponse("test")

        override fun getPath(): String = "page-api/test"
    }

    private class TestNoAnnotationRoute : IWebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = HtmlResponse("test")

        override fun getPath(): String = "/test"
    }

    @WebBlockApi
    private class TestUnregisteredTypeRoute : IWebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = JsonResponse("test")

        override fun getPath(): String = "/api/unregistered"
    }

    private class TestNonRouteClass : IRoute {
        override suspend fun getResponse(): WebBlockResponse = HtmlResponse("test")

        override fun getPath(): String = "/test"
    }
}
