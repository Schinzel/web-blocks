package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.JsonSuccessResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.routes.annotations.Api
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
class WebBlockPageRouteDescriptorTest {
    private val descriptor = WebBlockPageRouteDescriptor("io.schinzel.web_blocks.web.test_routes")

    @Nested
    @DisplayName("getRoutePath")
    inner class GetRoutePathTests {
        @Test
        fun `simple page class _ returns kebab case path`() {
            val path = descriptor.getRoutePath(io.schinzel.web_blocks.web.test_routes.pages.simple_page.MySimplePage::class)

            assertThat(path).isEqualTo("simple-page")
        }

        @Test
        fun `landing page class _ returns root path`() {
            val path = descriptor.getRoutePath(io.schinzel.web_blocks.web.test_routes.pages.landing.LandingPage::class)

            assertThat(path).isEqualTo("/")
        }

        @Test
        fun `page class with api prefix _ throws exception`() {
            val testDescriptor = WebBlockPageRouteDescriptor("io.schinzel.web_blocks.web.test_routes2")
            assertThatThrownBy {
                testDescriptor.getRoutePath(io.schinzel.web_blocks.web.test_routes2.pages.api.MyPage::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Page path cannot start with 'api'")
                .hasMessageContaining("Page path: 'api'")
        }

        @Test
        fun `page class with page-api prefix _ throws exception`() {
            val testDescriptor = WebBlockPageRouteDescriptor("io.schinzel.web_blocks.web.test_routes4")
            assertThatThrownBy {
                testDescriptor.getRoutePath(io.schinzel.web_blocks.web.test_routes4.page_api.static.MyPage::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Page path cannot start with 'page-api'")
                .hasMessageContaining("Page path: 'page-api/static'")
        }

        @Test
        fun `page class with static prefix _ throws exception`() {
            val testDescriptor = WebBlockPageRouteDescriptor("io.schinzel.web_blocks.web.test_routes3")
            assertThatThrownBy {
                testDescriptor.getRoutePath(io.schinzel.web_blocks.web.test_routes3.pages.static.MyPage::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Page path cannot start with 'static'")
                .hasMessageContaining("Page path: 'static'")
        }

        @Test
        fun `wrong annotation _ throws exception`() {
            assertThatThrownBy {
                descriptor.getRoutePath(TestWrongAnnotation::class)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("TestWrongAnnotation is not annotated with @WebBlockPage")
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

            assertThat(typeName).isEqualTo("WebBlockPageRoute")
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

    // Test classes for descriptor testing - these tests use the main test descriptor
    // For validation tests, we use specific test routes in appropriate package structures
    @Api
    private class TestWrongAnnotation : IWebBlockRoute {
        override suspend fun getResponse(): IWebBlockResponse = JsonSuccessResponse("test")

        override fun getPath(): String = "/api/test"
    }

    private class TestNoAnnotation : IWebBlockRoute {
        override suspend fun getResponse(): IWebBlockResponse = HtmlContentResponse("test")

        override fun getPath(): String = "/test"
    }

    private class TestNonRouteClass : IRoute {
        override suspend fun getResponse(): IWebBlockResponse = HtmlContentResponse("test")

        override fun getPath(): String = "/test"
    }
}
