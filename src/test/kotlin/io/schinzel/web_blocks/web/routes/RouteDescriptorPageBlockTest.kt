package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorPageBlock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test RouteDescriptorPageBlock for path generation.
 *
 * Written by Claude Sonnet 4
 */
class RouteDescriptorPageBlockTest {
    private val descriptor = RouteDescriptorPageBlock("io.schinzel.web_blocks.web.routes")

    @Nested
    @DisplayName("getRoutePathFromRelativePath")
    inner class GetRoutePathFromRelativePathTests {
        @Test
        fun `simple page block _ returns page-block prefixed path`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/user_pages/settings", "SavePersonNameBlock")
            assertThat(path).isEqualTo("/page-block/user-pages/settings/save-person-name")
        }

        @Test
        fun `page block with PageBlock suffix _ removes suffix`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/settings", "UpdateSettingsPageBlock")
            assertThat(path).isEqualTo("/page-block/settings/update-settings")
        }

        @Test
        fun `page block with Pb suffix _ removes suffix`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/user_page", "SaveNamePb")
            assertThat(path).isEqualTo("/page-block/user-page/save-name")
        }

        @Test
        fun `page block with PB suffix _ removes suffix`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/user_page", "SaveNamePB")
            assertThat(path).isEqualTo("/page-block/user-page/save-name")
        }

        @Test
        fun `page block with Block suffix _ removes suffix`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/dashboard", "UserStatsBlock")
            assertThat(path).isEqualTo("/page-block/dashboard/user-stats")
        }

        @Test
        fun `nested page block path _ converts directories to kebab case`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/user_pages/admin_settings", "DeleteUserBlock")
            assertThat(path).isEqualTo("/page-block/user-pages/admin-settings/delete-user")
        }

        @Test
        fun `PascalCase class name _ converts to kebab-case`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/user_management", "CreateNewUserAccountBlock")
            assertThat(path).isEqualTo("/page-block/user-management/create-new-user-account")
        }

        @Test
        fun `removes pages prefix from path`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/simple_page", "GreetingBlock")
            assertThat(path).isEqualTo("/page-block/simple-page/greeting")
        }
    }

    @Nested
    @DisplayName("getTypeName")
    inner class GetTypeNameTests {
        @Test
        fun `returns correct name`() {
            val typeName = descriptor.getTypeName()

            assertThat(typeName).isEqualTo("PageBlockRoute")
        }
    }

    @Nested
    @DisplayName("getReturnType")
    inner class GetReturnTypeTests {
        @Test
        fun `returns HTML`() {
            val returnType = descriptor.returnType

            assertThat(returnType).isEqualTo(ReturnTypeEnum.HTML)
        }
    }
}
