package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.response.JsonSuccessResponse
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.Page
import io.schinzel.web_blocks.web.routes.annotations.PageBlockApi
import io.schinzel.web_blocks.web.routes.annotations.RouteAnnotationUtil
import io.schinzel.web_blocks.web.routes.annotations.RouteTypeEnum
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test RouteAnnotationUtil for route type detection and validation.
 *
 * Written by Claude Sonnet 4
 */
class RouteAnnotationUtilTest {
    @Nested
    @DisplayName("detectRouteType")
    inner class DetectRouteTypeTests {
        @Test
        fun `Page annotated class _ returns PAGE`() {
            val result = RouteAnnotationUtil.detectRouteType(TestPageRoute::class)

            assertThat(result).isEqualTo(RouteTypeEnum.PAGE)
        }

        @Test
        fun `API annotated class _ returns API`() {
            val result = RouteAnnotationUtil.detectRouteType(TestApiRoute::class)

            assertThat(result).isEqualTo(RouteTypeEnum.API)
        }

        @Test
        fun `PageBlockApi annotated class _ returns PAGE_API`() {
            val result = RouteAnnotationUtil.detectRouteType(TestPageApiRoute::class)

            assertThat(result).isEqualTo(RouteTypeEnum.PAGE_BLOCK_API)
        }

        @Test
        fun `no annotation _ returns UNKNOWN`() {
            val result = RouteAnnotationUtil.detectRouteType(TestNoAnnotationRoute::class)

            assertThat(result).isEqualTo(RouteTypeEnum.UNKNOWN)
        }

        @Test
        fun `multiple annotations _ throws IllegalArgumentException`() {
            assertThatThrownBy {
                RouteAnnotationUtil.detectRouteType(TestMultipleAnnotationsRoute::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("TestMultipleAnnotationsRoute has multiple route annotations")
        }
    }

    // Test classes for route annotation testing
    @Page
    private class TestPageRoute : IHtmlRoute {
        override suspend fun getResponse(): IHtmlResponse = HtmlContentResponse("<h1>Test</h1>")
    }

    @Api
    private class TestApiRoute : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse(mapOf("test" to "value"))
    }

    @PageBlockApi
    private class TestPageApiRoute : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse(mapOf("test" to "value"))
    }

    private class TestNoAnnotationRoute : IWebBlockRoute<IWebBlockResponse> {
        override suspend fun getResponse(): IWebBlockResponse = HtmlContentResponse("<h1>Test</h1>")
    }

    @Page
    @Api
    private class TestMultipleAnnotationsRoute : IWebBlockRoute<IWebBlockResponse> {
        override suspend fun getResponse(): IWebBlockResponse = HtmlContentResponse("<h1>Test</h1>")
    }
}
