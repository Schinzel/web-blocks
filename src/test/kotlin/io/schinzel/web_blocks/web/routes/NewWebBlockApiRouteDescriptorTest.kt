package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.jsonSuccess
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test NewWebBlockApiRouteDescriptor for proper route path generation.
 *
 * Written by Claude Sonnet 4
 */
class NewWebBlockApiRouteDescriptorTest {
    private val endpointPackage = "io.schinzel.web_blocks.web.routes"
    private val descriptor = NewWebBlockApiRouteDescriptor(endpointPackage)

    @Nested
    @DisplayName("getRoutePath")
    inner class GetRoutePathTests {
        @Test
        fun `WebBlockApi annotated class _ generates path with web-block-api prefix`() {
            val result = descriptor.getRoutePath(TestWebBlockApiRoute::class)

            assertThat(result).startsWith("web-block-api/")
            assertThat(result).contains("test-web-block-api")
        }

        @Test
        fun `class not implementing IWebBlockRoute _ throws IllegalArgumentException`() {
            assertThatThrownBy {
                descriptor.getRoutePath(TestInvalidRoute::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("must implement IWebBlockRoute")
        }

        @Test
        fun `class without WebBlockApi annotation _ throws IllegalArgumentException`() {
            assertThatThrownBy {
                descriptor.getRoutePath(TestNoAnnotationRoute::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("has no route annotation")
        }
    }

    @Nested
    @DisplayName("getTypeName and getReturnType")
    inner class MetadataTests {
        @Test
        fun `getTypeName _ returns NewWebBlockApiRoute`() {
            assertThat(descriptor.getTypeName()).isEqualTo("NewWebBlockApiRoute")
        }

        @Test
        fun `getReturnType _ returns JSON`() {
            assertThat(descriptor.getReturnType()).isEqualTo(ReturnTypeEnum.JSON)
        }
    }

    // Test classes
    @WebBlockApi
    private class TestWebBlockApiRoute : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = jsonSuccess(mapOf("test" to "value"))

        override fun getPath(): String = "/api/test"
    }

    private class TestInvalidRoute : IRoute {
        override fun getPath(): String = "/test"
    }

    private class TestNoAnnotationRoute : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = jsonSuccess(mapOf("test" to "value"))

        override fun getPath(): String = "/api/test"
    }
}
