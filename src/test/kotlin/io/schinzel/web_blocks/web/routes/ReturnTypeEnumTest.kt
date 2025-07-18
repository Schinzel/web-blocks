package io.schinzel.web_blocks.web.routes

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test ReturnTypeEnum for route type mapping and content type handling.
 *
 * Written by Claude Sonnet 4
 */
class ReturnTypeEnumTest {
    @Nested
    @DisplayName("getReturnTypeFromRouteType")
    inner class GetReturnTypeFromRouteTypeTests {
        @Test
        fun `getReturnTypeFromRouteType _ PAGE route type _ returns HTML`() {
            val result = ReturnTypeEnum.getReturnTypeFromRouteType(RouteTypeEnum.PAGE)

            assertThat(result).isEqualTo(ReturnTypeEnum.HTML)
        }

        @Test
        fun `getReturnTypeFromRouteType _ API route type _ returns JSON`() {
            val result = ReturnTypeEnum.getReturnTypeFromRouteType(RouteTypeEnum.API)

            assertThat(result).isEqualTo(ReturnTypeEnum.JSON)
        }

        @Test
        fun `getReturnTypeFromRouteType _ PAGE_API route type _ returns JSON`() {
            val result = ReturnTypeEnum.getReturnTypeFromRouteType(RouteTypeEnum.PAGE_API)

            assertThat(result).isEqualTo(ReturnTypeEnum.JSON)
        }

        @Test
        fun `getReturnTypeFromRouteType _ UNKNOWN route type _ throws IllegalArgumentException`() {
            assertThatThrownBy {
                ReturnTypeEnum.getReturnTypeFromRouteType(RouteTypeEnum.UNKNOWN)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Cannot determine return type for unknown route type")
        }
    }

    @Nested
    @DisplayName("getContentType")
    inner class GetContentTypeTests {
        @Test
        fun `getContentType _ HTML return type _ returns text slash html`() {
            val result = ReturnTypeEnum.HTML.getContentType()

            assertThat(result).isEqualTo("text/html")
        }

        @Test
        fun `getContentType _ JSON return type _ returns application slash json`() {
            val result = ReturnTypeEnum.JSON.getContentType()

            assertThat(result).isEqualTo("application/json")
        }
    }

    @Nested
    @DisplayName("Integration tests")
    inner class IntegrationTests {
        @Test
        fun `route type to content type _ PAGE annotation _ produces text slash html`() {
            val routeType = RouteTypeEnum.PAGE
            val returnType = ReturnTypeEnum.getReturnTypeFromRouteType(routeType)
            val contentType = returnType.getContentType()

            assertThat(contentType).isEqualTo("text/html")
        }

        @Test
        fun `route type to content type _ API annotation _ produces application slash json`() {
            val routeType = RouteTypeEnum.API
            val returnType = ReturnTypeEnum.getReturnTypeFromRouteType(routeType)
            val contentType = returnType.getContentType()

            assertThat(contentType).isEqualTo("application/json")
        }

        @Test
        fun `route type to content type _ PAGE_API annotation _ produces application slash json`() {
            val routeType = RouteTypeEnum.PAGE_API
            val returnType = ReturnTypeEnum.getReturnTypeFromRouteType(routeType)
            val contentType = returnType.getContentType()

            assertThat(contentType).isEqualTo("application/json")
        }

        @Test
        fun `all route types _ except UNKNOWN _ map to valid return types`() {
            val validRouteTypes = listOf(RouteTypeEnum.PAGE, RouteTypeEnum.API, RouteTypeEnum.PAGE_API)

            validRouteTypes.forEach { routeType ->
                val returnType = ReturnTypeEnum.getReturnTypeFromRouteType(routeType)
                val contentType = returnType.getContentType()

                assertThat(returnType).isIn(ReturnTypeEnum.HTML, ReturnTypeEnum.JSON)
                assertThat(contentType).isIn("text/html", "application/json")
            }
        }
    }
}
