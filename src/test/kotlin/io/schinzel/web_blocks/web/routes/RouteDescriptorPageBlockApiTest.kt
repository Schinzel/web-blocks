package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.response.JsonSuccessResponse
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.PageBlockApi
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorPageBlockApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test WebBlockPageApiRouteDescriptor for path generation and validation.
 *
 * Written by Claude Sonnet 4
 */
class RouteDescriptorPageBlockApiTest {
    private val descriptor = RouteDescriptorPageBlockApi("io.schinzel.sample")

    @Nested
    @DisplayName("getRoutePathFromRelativePath")
    inner class GetRoutePathFromRelativePathTests {
        @Test
        fun `page api path _ returns page-block-api prefixed path with directory and class name`() {
            val path =
                descriptor.getRoutePathFromRelativePath(
                    "pages/page_with_blocks_and_page_api_route/blocks/update_name_block",
                    "UpdateFirstNamePageBlockApi",
                )
            assertThat(path).isEqualTo("/page-block-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name")
        }

        @Test
        fun `simple page api path _ returns page-block-api prefixed path`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/user_page", "SaveNameApi")
            assertThat(path).isEqualTo("/page-block-api/user-page/save-name")
        }

        @Test
        fun `class name with suffix _ removes suffix`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/settings", "UpdateSettingsPageBlockApi")
            assertThat(path).isEqualTo("/page-block-api/settings/update-settings")
        }

        @Test
        fun `nested directory path _ converts to kebab case`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/user_pages/admin_settings", "DeleteUserApi")
            assertThat(path).isEqualTo("/page-block-api/user-pages/admin-settings/delete-user")
        }
    }

    @Nested
    @DisplayName("getTypeName")
    inner class GetTypeNameTests {
        @Test
        fun `returns correct name`() {
            val typeName = descriptor.getTypeName()

            assertThat(typeName).isEqualTo("PageBlockApiRoute")
        }
    }

    @Nested
    @DisplayName("getReturnType")
    inner class GetReturnTypeTests {
        @Test
        fun `returns JSON`() {
            val returnType = descriptor.returnType
            assertThat(returnType).isEqualTo(ReturnTypeEnum.JSON)
        }
    }

    // Test classes for descriptor testing
    @PageBlockApi
    private class TestSimplePageJson : IJsonRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse("test")
    }

    @PageBlockApi
    private class TestPageJsonRoute : IJsonRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse("test")
    }

    @PageBlockApi
    private class TestSubdirPageJson : IJsonRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse("test")
    }

    @Api
    private class TestWrongAnnotation : IJsonRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse("test")
    }

    private class TestNoAnnotation : IWebBlockRoute<IWebBlockResponse> {
        override suspend fun getResponse(): IWebBlockResponse = JsonSuccessResponse("test")
    }

    private class TestNonRouteClass : IRoute
}
