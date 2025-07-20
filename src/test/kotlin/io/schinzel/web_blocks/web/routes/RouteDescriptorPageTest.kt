package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorPage
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test WebBlockPageRouteDescriptor for path generation and validation.
 *
 * Written by Claude Sonnet 4
 */
class RouteDescriptorPageTest {
    private val descriptor = RouteDescriptorPage("io.schinzel.web_blocks.web.test_routes")

    @Nested
    @DisplayName("getRoutePathFromRelativePath")
    inner class GetRoutePathFromRelativePathTests {
        @Test
        fun `simple page path _ returns kebab case path`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/simple_page", "MySimplePage")
            assertThat(path).isEqualTo("/simple-page")
        }

        @Test
        fun `landing page path _ returns root path`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/landing", "LandingPage")
            assertThat(path).isEqualTo("/")
        }

        @Test
        fun `nested page path _ returns kebab case path`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/user_pages/settings", "UserSettingsPage")
            assertThat(path).isEqualTo("/user-pages/settings")
        }

        @Test
        fun `class name with suffix _ removes suffix`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/simple_page", "MySimplePageRoute")
            assertThat(path).isEqualTo("/simple-page")
        }

        @Test
        fun `page path with api prefix _ throws exception`() {
            assertThatThrownBy {
                descriptor.getRoutePathFromRelativePath("pages/api", "MyPage")
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `page path with page-api prefix _ throws exception`() {
            assertThatThrownBy {
                descriptor.getRoutePathFromRelativePath("pages/page_block/static", "MyPage")
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `page path with static prefix _ throws exception`() {
            assertThatThrownBy {
                descriptor.getRoutePathFromRelativePath("pages/static", "MyPage")
            }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    @DisplayName("getTypeName")
    inner class GetTypeNameTests {
        @Test
        fun `returns correct name`() {
            val typeName = descriptor.getTypeName()

            assertThat(typeName).isEqualTo("PageRoute")
        }
    }

    @Nested
    @DisplayName("getReturnType")
    inner class GetReturnTypeTests {
        @Test
        fun `returns HTML`() {
            val returnType = descriptor.getReturnType()

            assertThat(returnType).isEqualTo(ReturnTypeEnum.HTML)
        }
    }
}
