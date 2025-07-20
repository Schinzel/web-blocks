package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.annotations.WebBlock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test WebBlockRouteDescriptor for proper route path generation.
 *
 * Written by Claude Sonnet 4
 */
class WebBlockRouteDescriptorTest {
    private val endpointPackage = "io.schinzel.web_blocks.web.routes"
    private val descriptor = WebBlockRouteDescriptor(endpointPackage)

    @Nested
    @DisplayName("getRoutePath")
    inner class GetRoutePathTests {
        @Test
        fun `WebBlock annotated class _ generates path with web-block prefix`() {
            val result = descriptor.getRoutePath(TestWebBlockRoute::class)

            assertThat(result).startsWith("web-block/")
            assertThat(result).contains("test-web-block")
        }

        @Test
        fun `class not implementing IWebBlockRoute _ throws IllegalArgumentException`() {
            assertThatThrownBy {
                descriptor.getRoutePath(TestInvalidRoute::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("must implement IWebBlockRoute")
        }

        @Test
        fun `class without WebBlock annotation _ throws IllegalArgumentException`() {
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
        fun `getTypeName _ returns WebBlockRoute`() {
            assertThat(descriptor.getTypeName()).isEqualTo("WebBlockRoute")
        }

        @Test
        fun `getReturnType _ returns HTML`() {
            assertThat(descriptor.getReturnType()).isEqualTo(ReturnTypeEnum.HTML)
        }
    }

    // Test classes
    @WebBlock
    private class TestWebBlockRoute : IHtmlRoute {
        override suspend fun getResponse(): IHtmlResponse = html("<h1>Test</h1>")

        override fun getPath(): String = "/test"
    }

    private class TestInvalidRoute : IRoute {
        override fun getPath(): String = "/test"
    }

    private class TestNoAnnotationRoute : IHtmlRoute {
        override suspend fun getResponse(): IHtmlResponse = html("<h1>Test</h1>")

        override fun getPath(): String = "/test"
    }
}
