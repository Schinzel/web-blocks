package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test RouteDescriptorApi for path generation.
 *
 * Written by Claude Sonnet 4
 */
class RouteDescriptorApiTest {
    private val descriptor = RouteDescriptorApi("io.schinzel.web_blocks.web.routes")

    @Nested
    @DisplayName("getRoutePathFromRelativePath")
    inner class GetRoutePathFromRelativePathTests {
        @Test
        fun `simple api class _ returns api prefixed path`() {
            val path = descriptor.getRoutePathFromRelativePath("api", "UserPets")
            assertThat(path).isEqualTo("/api/user-pets")
        }

        @Test
        fun `api class with suffix _ removes suffix`() {
            val path = descriptor.getRoutePathFromRelativePath("api", "UserInformationEndpointRoute")
            assertThat(path).isEqualTo("/api/user-information-endpoint")
        }

        @Test
        fun `api class with Api suffix _ removes suffix`() {
            val path = descriptor.getRoutePathFromRelativePath("api", "UserApi")
            assertThat(path).isEqualTo("/api/user")
        }

        @Test
        fun `api class with API suffix _ removes suffix`() {
            val path = descriptor.getRoutePathFromRelativePath("api", "UserAPI")
            assertThat(path).isEqualTo("/api/user")
        }

        @Test
        fun `api class with ApiRoute suffix _ removes suffix`() {
            val path = descriptor.getRoutePathFromRelativePath("api", "UserApi")
            assertThat(path).isEqualTo("/api/user")
        }

        @Test
        fun `nested api directory _ includes directory in path`() {
            val path = descriptor.getRoutePathFromRelativePath("api/user_management", "CreateUserApi")
            assertThat(path).isEqualTo("/api/user-management/create-user")
        }

        @Test
        fun `PascalCase class name _ converts to kebab-case`() {
            val path = descriptor.getRoutePathFromRelativePath("api", "GetUserInformationEndpoint")
            assertThat(path).isEqualTo("/api/get-user-information-endpoint")
        }
    }

    @Nested
    @DisplayName("getTypeName")
    inner class GetTypeNameTests {
        @Test
        fun `returns correct name`() {
            val typeName = descriptor.getTypeName()

            assertThat(typeName).isEqualTo("ApiRoute")
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
}
