package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorPage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test WebBlockPageRouteDescriptor basic functionality.
 *
 * Written by Claude Sonnet 4
 */
class RouteDescriptorPageSimpleTest {
    private val descriptor = RouteDescriptorPage("io.schinzel.web_blocks.web.routes")

    @Nested
    @DisplayName("Basic functionality")
    inner class BasicFunctionalityTests {
        @Test
        fun `returns correct name`() {
            val typeName = descriptor.getTypeName()
            assertThat(typeName).isEqualTo("PageRoute")
        }

        @Test
        fun `returns HTML`() {
            val returnType = descriptor.returnType
            assertThat(returnType).isEqualTo(ReturnTypeEnum.HTML)
        }

        @Test
        fun `simple page path _ returns path`() {
            val path = descriptor.getRoutePathFromRelativePath("pages/simple_page", "MyPage")
            assertThat(path).isNotNull
        }
    }
}
