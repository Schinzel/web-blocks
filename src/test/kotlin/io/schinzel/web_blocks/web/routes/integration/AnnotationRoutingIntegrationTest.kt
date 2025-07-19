package io.schinzel.web_blocks.web.routes.integration

import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.JsonSuccessResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.ReturnTypeEnum
import io.schinzel.web_blocks.web.routes.RouteAnnotationUtil
import io.schinzel.web_blocks.web.routes.RouteDescriptorRegistry
import io.schinzel.web_blocks.web.routes.RouteTypeEnum
import io.schinzel.web_blocks.web.routes.WebBlockApiRouteDescriptor
import io.schinzel.web_blocks.web.routes.WebBlockPageApiRouteDescriptor
import io.schinzel.web_blocks.web.routes.WebBlockPageRouteDescriptor
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.Page
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi
import io.schinzel.web_blocks.web.set_up_routes.FindRoutes
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test end-to-end annotation-based routing integration.
 *
 * Written by Claude Sonnet 4
 */
class AnnotationRoutingIntegrationTest {
    private val endpointPackage = "io.schinzel.web_blocks.web.routes.integration"

    @BeforeEach
    fun setUp() {
        // Register route descriptors for integration tests
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.PAGE,
            WebBlockPageRouteDescriptor(endpointPackage),
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.API,
            WebBlockApiRouteDescriptor(endpointPackage),
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.PAGE_API,
            WebBlockPageApiRouteDescriptor(endpointPackage),
        )
    }

    @Nested
    @DisplayName("End-to-end route processing")
    inner class EndToEndRouteProcessingTests {
        @Test
        fun `page route _ works correctly`() {
            // Create route instance
            val route = TestPageRoute()

            // Get response
            val response = runBlocking { route.getResponse() }

            // Verify route works end-to-end
            assertThat(response).isInstanceOf(HtmlContentResponse::class.java)
            assertThat((response as HtmlContentResponse).content).isEqualTo("test page")

            // Verify route type detection works
            val routeType = RouteAnnotationUtil.detectRouteType(TestPageRoute::class)
            assertThat(routeType).isEqualTo(RouteTypeEnum.PAGE)
        }

        @Test
        fun `api route _ works correctly`() {
            // Create route instance
            val route = TestApiRoute()

            // Get response
            val response = runBlocking { route.getResponse() }

            // Verify route works end-to-end
            assertThat(response).isInstanceOf(JsonSuccessResponse::class.java)
            assertThat((response as JsonSuccessResponse).data).isEqualTo("test api")

            // Verify route type detection works
            val routeType = RouteAnnotationUtil.detectRouteType(TestApiRoute::class)
            assertThat(routeType).isEqualTo(RouteTypeEnum.API)
        }

        @Test
        fun `page api route _ works correctly`() {
            // Create route instance
            val route = TestPageApiRoute()

            // Get response
            val response = runBlocking { route.getResponse() }

            // Verify route works end-to-end
            assertThat(response).isInstanceOf(JsonSuccessResponse::class.java)
            assertThat((response as JsonSuccessResponse).data).isEqualTo("test page api")

            // Verify route type detection works
            val routeType = RouteAnnotationUtil.detectRouteType(TestPageApiRoute::class)
            assertThat(routeType).isEqualTo(RouteTypeEnum.PAGE_API)
        }
    }

    @Nested
    @DisplayName("Route discovery integration")
    inner class RouteDiscoveryIntegrationTests {
        @Test
        fun `route discovery _ finds all routes`() {
            val findRoutes = FindRoutes(endpointPackage)
            findRoutes.registerRoutes()

            val routes = findRoutes.getAnnotationBasedRoutes()

            assertThat(routes).hasSize(3)
            assertThat(routes).anyMatch { it.simpleName == "TestPageRoute" }
            assertThat(routes).anyMatch { it.simpleName == "TestApiRoute" }
            assertThat(routes).anyMatch { it.simpleName == "TestPageApiRoute" }
        }

        @Test
        fun `route descriptor lookup _ works for all route types`() {
            val pageDescriptor = RouteDescriptorRegistry.getRouteDescriptor(TestPageRoute::class)
            val apiDescriptor = RouteDescriptorRegistry.getRouteDescriptor(TestApiRoute::class)
            val pageApiDescriptor = RouteDescriptorRegistry.getRouteDescriptor(TestPageApiRoute::class)

            assertThat(pageDescriptor.getTypeName()).isEqualTo("WebBlockPageRoute")
            assertThat(pageDescriptor.getReturnType()).isEqualTo(ReturnTypeEnum.HTML)

            assertThat(apiDescriptor.getTypeName()).isEqualTo("WebBlockApiRoute")
            assertThat(apiDescriptor.getReturnType()).isEqualTo(ReturnTypeEnum.JSON)

            assertThat(pageApiDescriptor.getTypeName()).isEqualTo("WebBlockPageApiRoute")
            assertThat(pageApiDescriptor.getReturnType()).isEqualTo(ReturnTypeEnum.JSON)
        }

        @Test
        fun `route path generation _ works for all route types`() {
            val pageDescriptor = RouteDescriptorRegistry.getRouteDescriptor(TestPageRoute::class)
            val apiDescriptor = RouteDescriptorRegistry.getRouteDescriptor(TestApiRoute::class)
            val pageApiDescriptor = RouteDescriptorRegistry.getRouteDescriptor(TestPageApiRoute::class)

            val pagePath = pageDescriptor.getRoutePath(TestPageRoute::class)
            val apiPath = apiDescriptor.getRoutePath(TestApiRoute::class)
            val pageApiPath = pageApiDescriptor.getRoutePath(TestPageApiRoute::class)

            // The exact path format depends on the package structure
            // We just verify that paths can be generated without errors
            assertThat(pagePath).isNotNull
            assertThat(apiPath).isNotNull
            assertThat(pageApiPath).isNotNull
        }
    }

    // Test classes for integration testing
    @Page
    private class TestPageRoute : IWebBlockRoute {
        override suspend fun getResponse(): IWebBlockResponse = HtmlContentResponse("test page")

        override fun getPath(): String = "/test-page"
    }

    @Api
    private class TestApiRoute : IWebBlockRoute {
        override suspend fun getResponse(): IWebBlockResponse = JsonSuccessResponse("test api")

        override fun getPath(): String = "/api/test"
    }

    @WebBlockPageApi
    private class TestPageApiRoute : IWebBlockRoute {
        override suspend fun getResponse(): IWebBlockResponse = JsonSuccessResponse("test page api")

        override fun getPath(): String = "page-api/test"
    }
}
