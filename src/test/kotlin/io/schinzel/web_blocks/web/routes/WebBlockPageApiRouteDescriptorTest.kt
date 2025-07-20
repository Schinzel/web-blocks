package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.response.JsonSuccessResponse
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorApi
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test WebBlockPageApiRouteDescriptor for path generation and validation.
 *
 * Written by Claude Sonnet 4
 */
class WebBlockPageApiRouteDescriptorTest {
    private val descriptor = RouteDescriptorApi("io.schinzel.sample")

    @Nested
    @DisplayName("getRoutePath")
    inner class GetRoutePathTests {
        @Test
        fun `page api class _ returns page-api prefixed path with directory and class name`() {
            val path = descriptor.getRoutePath(io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block.UpdateFirstNameRoute::class)

            assertThat(path).isEqualTo("page-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name")
        }

        // Additional tests would require more sample PageApi routes

        @Test
        fun `wrong annotation _ throws exception`() {
            assertThatThrownBy {
                descriptor.getRoutePath(TestWrongAnnotation::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("TestWrongAnnotation is not annotated with @WebBlockPageApi")
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

    @Nested
    @DisplayName("getTypeName")
    inner class GetTypeNameTests {
        @Test
        fun `returns correct name`() {
            val typeName = descriptor.getTypeName()

            assertThat(typeName).isEqualTo("WebBlockPageApiRoute")
        }
    }

    @Nested
    @DisplayName("getReturnType")
    inner class GetReturnTypeTests {
        @Test
        fun `returns JSON`() {
            val returnType = descriptor.getReturnType()

            assertThat(returnType).isEqualTo(ReturnTypeEnum.JSON)
        }
    }

    // Test classes for descriptor testing
    @WebBlockPageApi
    private class TestSimplePageApi : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse("test")
    }

    @WebBlockPageApi
    private class TestPageApiRoute : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse("test")
    }

    @WebBlockPageApi
    private class TestSubdirPageApi : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse("test")
    }

    @Api
    private class TestWrongAnnotation : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse("test")
    }

    private class TestNoAnnotation : IWebBlockRoute<IWebBlockResponse> {
        override suspend fun getResponse(): IWebBlockResponse = JsonSuccessResponse("test")
    }

    private class TestNonRouteClass : IRoute
}
