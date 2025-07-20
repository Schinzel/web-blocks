package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.response.JsonSuccessResponse
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.Page
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorWebBlockApi
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test WebBlockApiRouteDescriptor for path generation and validation.
 *
 * Written by Claude Sonnet 4
 */
class WebBlockApiRouteDescriptorTest {
    private val descriptor = RouteDescriptorWebBlockApi("io.schinzel.sample")

    @Nested
    @DisplayName("getRoutePath")
    inner class GetRoutePathTests {
        @Test
        fun `simple api class _ returns class name only`() {
            val path = descriptor.getRoutePath(io.schinzel.sample.api.UserPets::class)

            assertThat(path).isEqualTo("api/user-pets")
        }

        @Test
        fun `api class with endpoint suffix _ returns class name`() {
            val path = descriptor.getRoutePath(io.schinzel.sample.api.UserInformationEndpoint::class)

            assertThat(path).isEqualTo("api/user-information-endpoint")
        }

        @Test
        fun `api class with long name _ returns kebab case class name`() {
            val path = descriptor.getRoutePath(io.schinzel.sample.api.ApiRouteThatThrowsError::class)

            assertThat(path).isEqualTo("api/api-route-that-throws-error")
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

    @Nested
    @DisplayName("getTypeName")
    inner class GetTypeNameTests {
        @Test
        fun `returns correct name`() {
            val typeName = descriptor.getTypeName()

            assertThat(typeName).isEqualTo("WebBlockApiRoute")
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
    @Api
    private class TestSimpleApi : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse("test")
    }

    @Api
    private class TestApiRoute : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = JsonSuccessResponse("test")
    }

    @Api
    private class TestSubdirApi : IApiRoute {
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
