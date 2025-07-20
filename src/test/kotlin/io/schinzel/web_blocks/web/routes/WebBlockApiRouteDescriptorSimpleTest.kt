package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.response.JsonSuccessResponse
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.Page
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test WebBlockApiRouteDescriptor basic functionality.
 *
 * Written by Claude Sonnet 4
 */
class WebBlockApiRouteDescriptorSimpleTest {
    private val descriptor = WebBlockApiRouteDescriptor("io.schinzel.web_blocks.web.routes")

    @Nested
    @DisplayName("Basic functionality")
    inner class BasicFunctionalityTests {
        @Test
        fun `returns correct name`() {
            val typeName = descriptor.getTypeName()

            assertThat(typeName).isEqualTo("WebBlockApiRoute")
        }

        @Test
        fun `returns JSON`() {
            val returnType = descriptor.getReturnType()

            assertThat(returnType).isEqualTo(ReturnTypeEnum.JSON)
        }

        @Test
        fun `api annotated route _ does not throw exception`() {
            // This test verifies that the method can be called without throwing an exception
            val path = descriptor.getRoutePath(TestApiRoute::class)

            assertThat(path).isNotNull
        }

        @Test
        fun `wrong annotation _ throws exception`() {
            assertThatThrownBy {
                descriptor.getRoutePath(TestWrongAnnotation::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("TestWrongAnnotation is not annotated with @WebBlockApi")
        }

        @Test
        fun `no annotation _ throws exception`() {
            assertThatThrownBy {
                descriptor.getRoutePath(TestNoAnnotation::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("TestNoAnnotation implements IWebBlockRoute but has no route annotation")
        }

        @Test
        fun `non-route class _ throws exception`() {
            assertThatThrownBy {
                descriptor.getRoutePath(TestNonRouteClass::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("TestNonRouteClass must implement IWebBlockRoute")
        }
    }

    // Test classes for descriptor testing
    @Api
    private class TestApiRoute : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse("test")
    }

    @Page
    private class TestWrongAnnotation : IHtmlRoute {
        override suspend fun getResponse(): IHtmlResponse = HtmlContentResponse("test")
    }

    private class TestNoAnnotation : IWebBlockRoute<IWebBlockResponse> {
        override suspend fun getResponse(): IWebBlockResponse = JsonSuccessResponse("test")
    }

    private class TestNonRouteClass : IRoute
}
