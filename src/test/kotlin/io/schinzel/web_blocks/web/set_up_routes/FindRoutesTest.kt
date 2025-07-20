package io.schinzel.web_blocks.web.set_up_routes

import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.response.jsonSuccess
import io.schinzel.web_blocks.web.routes.IApiRoute
import io.schinzel.web_blocks.web.routes.IHtmlRoute
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.Page
import io.schinzel.web_blocks.web.routes.annotations.WebBlock
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test FindRoutes for proper route discovery and registration.
 *
 * Written by Claude Sonnet 4
 */
class FindRoutesTest {
    private val testPackage = "io.schinzel.web_blocks.web.set_up_routes"
    private lateinit var findRoutes: FindRoutes

    @BeforeEach
    fun setUp() {
        findRoutes = FindRoutes(testPackage)
    }

    @Nested
    @DisplayName("registerRoutes")
    inner class RegisterRoutesTests {
        @Test
        fun `registerRoutes _ completes without error`() {
            // This should not throw any exceptions
            findRoutes.registerRoutes()

            // Just verify the method runs successfully
            assertThat(true).isTrue
        }
    }

    @Nested
    @DisplayName("findRoutes function")
    inner class FindRoutesFunctionTests {
        @Test
        fun `findRoutes _ returns route mappings`() {
            val routes = findRoutes(testPackage)

            // Should return some routes (at least the test routes in this class)
            assertThat(routes).isNotEmpty
        }

        @Test
        fun `findRoutes _ includes test routes from this class`() {
            val routes = findRoutes(testPackage)

            val routeClassNames = routes.map { it.routeClass.simpleName }

            // Should find some of our test classes
            assertThat(routeClassNames).contains("TestValidPageRoute")
        }
    }

    // Valid test classes for the route discovery system to find
    @Page
    private class TestValidPageRoute : IHtmlRoute {
        override suspend fun getResponse(): IHtmlResponse = html("<h1>Test</h1>")

        override fun getPath(): String = "/test"
    }

    @Api
    private class TestValidApiRoute : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = jsonSuccess(mapOf("test" to "value"))

        override fun getPath(): String = "/api/test"
    }

    @WebBlock
    private class TestValidWebBlockRoute : IHtmlRoute {
        override suspend fun getResponse(): IHtmlResponse = html("<h1>WebBlock Test</h1>")

        override fun getPath(): String = "/web-block/test"
    }

    @WebBlockApi
    private class TestValidWebBlockApiRoute : IApiRoute {
        override suspend fun getResponse(): IJsonResponse = jsonSuccess(mapOf("webblock" to "api"))

        override fun getPath(): String = "/web-block-api/test"
    }
}
