package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.response.jsonSuccess
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.Page
import io.schinzel.web_blocks.web.routes.annotations.WebBlock
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test RouteDescriptorRegistry for proper registration and retrieval.
 *
 * Written by Claude Sonnet 4
 */
class RouteDescriptorRegistryTest {
    private val testEndpointPackage = "io.schinzel.web_blocks.web.routes"

    @Nested
    @DisplayName("registerAnnotation and getRouteDescriptor")
    inner class RegistrationTests {
        @Test
        fun `registerAnnotation _ stores descriptor for route type`() {
            val descriptor = RouteDescriptorPage(testEndpointPackage)

            RouteDescriptorRegistry.registerAnnotation(RouteTypeEnum.PAGE, descriptor)

            // Test that we can get a route descriptor for a class
            val retrieved = RouteDescriptorRegistry.getRouteDescriptor(TestPageRoute::class)
            assertThat(retrieved).isNotNull
        }

        @Test
        fun `getRouteDescriptor for class without annotation _ throws IllegalArgumentException`() {
            assertThatThrownBy {
                RouteDescriptorRegistry.getRouteDescriptor(TestUnknownRoute::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("has no WebBlock annotation")
        }
    }

    @Nested
    @DisplayName("integration with route descriptors")
    inner class IntegrationTests {
        @Test
        fun `register all route types _ all can be retrieved via classes`() {
            RouteDescriptorRegistry.registerAnnotation(
                RouteTypeEnum.PAGE,
                RouteDescriptorPage(testEndpointPackage),
            )
            RouteDescriptorRegistry.registerAnnotation(
                RouteTypeEnum.API,
                WebBlockApiRouteDescriptor(testEndpointPackage),
            )
            RouteDescriptorRegistry.registerAnnotation(
                RouteTypeEnum.WEB_BLOCK,
                WebBlockRouteDescriptor(testEndpointPackage),
            )
            RouteDescriptorRegistry.registerAnnotation(
                RouteTypeEnum.WEB_BLOCK_API,
                NewWebBlockApiRouteDescriptor(testEndpointPackage),
            )

            assertThat(RouteDescriptorRegistry.getRouteDescriptor(TestPageRoute::class)).isNotNull
            assertThat(RouteDescriptorRegistry.getRouteDescriptor(TestApiRoute::class)).isNotNull
            assertThat(RouteDescriptorRegistry.getRouteDescriptor(TestWebBlockRoute::class)).isNotNull
            assertThat(RouteDescriptorRegistry.getRouteDescriptor(TestWebBlockApiRoute::class)).isNotNull
        }
    }

    // Test classes
    @Page
    private class TestPageRoute : IHtmlRoute {
        override suspend fun getResponse(): IHtmlResponse = html("<h1>Test</h1>")

        override fun getPath(): String = "/test"
    }

    @Api
    private class TestApiRoute : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = jsonSuccess(mapOf("test" to "value"))

        override fun getPath(): String = "/api/test"
    }

    @WebBlock
    private class TestWebBlockRoute : IHtmlRoute {
        override suspend fun getResponse(): IHtmlResponse = html("<h1>Test</h1>")

        override fun getPath(): String = "/web-block/test"
    }

    @WebBlockApi
    private class TestWebBlockApiRoute : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = jsonSuccess(mapOf("test" to "value"))

        override fun getPath(): String = "/web-block-api/test"
    }

    private class TestUnknownRoute : IHtmlRoute {
        override suspend fun getResponse(): IHtmlResponse = html("<h1>Test</h1>")

        override fun getPath(): String = "/unknown"
    }
}
