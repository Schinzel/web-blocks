package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.response.JsonSuccessResponse
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.Page
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi
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
        fun `WebBlockPage annotated class _ returns PAGE`() {
            val result = RouteAnnotationUtil.detectRouteType(TestPageRoute::class)

            assertThat(result).isEqualTo(RouteTypeEnum.PAGE)
        }

        @Test
        fun `WebBlockApi annotated class _ returns API`() {
            val result = RouteAnnotationUtil.detectRouteType(TestApiRoute::class)

            assertThat(result).isEqualTo(RouteTypeEnum.API)
        }

        @Test
        fun `WebBlockPageApi annotated class _ returns PAGE_API`() {
            val result = RouteAnnotationUtil.detectRouteType(TestPageApiRoute::class)

            assertThat(result).isEqualTo(RouteTypeEnum.PAGE_API)
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
                .hasMessageContaining("Only one of @WebBlockPage, @WebBlockApi, or @WebBlockPageApi is allowed")
        }
    }

    @Nested
    @DisplayName("validateRouteAnnotation")
    inner class ValidateRouteAnnotationTests {
        @Test
        fun `WebBlockPage annotated route _ passes validation`() {
            RouteAnnotationUtil.validateRouteAnnotation(TestPageRoute::class)
        }

        @Test
        fun `WebBlockApi annotated route _ passes validation`() {
            RouteAnnotationUtil.validateRouteAnnotation(TestApiRoute::class)
        }

        @Test
        fun `WebBlockPageApi annotated route _ passes validation`() {
            RouteAnnotationUtil.validateRouteAnnotation(TestPageApiRoute::class)
        }

        @Test
        fun `no annotation _ throws IllegalArgumentException`() {
            assertThatThrownBy {
                RouteAnnotationUtil.validateRouteAnnotation(TestNoAnnotationRoute::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("TestNoAnnotationRoute implements IWebBlockRoute but has no route annotation")
                .hasMessageContaining("Add @WebBlockPage, @WebBlockApi, or @WebBlockPageApi annotation")
        }

        @Test
        fun `multiple annotations _ throws IllegalArgumentException`() {
            assertThatThrownBy {
                RouteAnnotationUtil.validateRouteAnnotation(TestMultipleAnnotationsRoute::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("TestMultipleAnnotationsRoute has multiple route annotations")
        }
    }

    @Nested
    @DisplayName("RouteTypeEnum")
    inner class RouteTypeEnumTests {
        @Test
        fun `PAGE _ returns text slash html`() {
            assertThat(RouteTypeEnum.PAGE.contentType).isEqualTo("text/html")
        }

        @Test
        fun `API _ returns application slash json`() {
            assertThat(RouteTypeEnum.API.contentType).isEqualTo("application/json")
        }

        @Test
        fun `PAGE_API _ returns application slash json`() {
            assertThat(RouteTypeEnum.PAGE_API.contentType).isEqualTo("application/json")
        }

        @Test
        fun `UNKNOWN _ returns application slash octet-stream`() {
            assertThat(RouteTypeEnum.UNKNOWN.contentType).isEqualTo("application/octet-stream")
        }

        @Test
        fun `PAGE with HtmlResponse _ returns true`() {
            val response = HtmlContentResponse("<h1>Test</h1>")

            assertThat(RouteTypeEnum.PAGE.isValidResponseType(response)).isTrue
        }

        @Test
        fun `PAGE with JsonResponse _ returns false`() {
            val response = JsonSuccessResponse(mapOf("test" to "value"))

            assertThat(RouteTypeEnum.PAGE.isValidResponseType(response)).isFalse
        }

        @Test
        fun `API with JsonResponse _ returns true`() {
            val response = JsonSuccessResponse(mapOf("test" to "value"))

            assertThat(RouteTypeEnum.API.isValidResponseType(response)).isTrue
        }

        @Test
        fun `API with HtmlResponse _ returns false`() {
            val response = HtmlContentResponse("<h1>Test</h1>")

            assertThat(RouteTypeEnum.API.isValidResponseType(response)).isFalse
        }

        @Test
        fun `PAGE_API with JsonResponse _ returns true`() {
            val response = JsonSuccessResponse(mapOf("test" to "value"))

            assertThat(RouteTypeEnum.PAGE_API.isValidResponseType(response)).isTrue
        }

        @Test
        fun `PAGE_API with HtmlResponse _ returns false`() {
            val response = HtmlContentResponse("<h1>Test</h1>")

            assertThat(RouteTypeEnum.PAGE_API.isValidResponseType(response)).isFalse
        }

        @Test
        fun `UNKNOWN with any response _ returns false`() {
            val htmlResponse = HtmlContentResponse("<h1>Test</h1>")
            val jsonSuccessResponse = JsonSuccessResponse(mapOf("test" to "value"))

            assertThat(RouteTypeEnum.UNKNOWN.isValidResponseType(htmlResponse)).isFalse
            assertThat(RouteTypeEnum.UNKNOWN.isValidResponseType(jsonSuccessResponse)).isFalse
        }

        @Test
        fun `PAGE _ returns HtmlResponse`() {
            assertThat(RouteTypeEnum.PAGE.getExpectedResponseType()).isEqualTo("HtmlResponse")
        }

        @Test
        fun `API _ returns JsonResponse`() {
            assertThat(RouteTypeEnum.API.getExpectedResponseType()).isEqualTo("JsonResponse")
        }

        @Test
        fun `PAGE_API _ returns JsonResponse`() {
            assertThat(RouteTypeEnum.PAGE_API.getExpectedResponseType()).isEqualTo("JsonResponse")
        }

        @Test
        fun `UNKNOWN _ returns UnknownResponse`() {
            assertThat(RouteTypeEnum.UNKNOWN.getExpectedResponseType()).isEqualTo("UnknownResponse")
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

    @WebBlockPageApi
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
