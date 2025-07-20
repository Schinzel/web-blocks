package io.schinzel.web_blocks.web.routes.annotations

/**
 * The purpose of this annotation is to mark a class as a WebBlock API route
 * that provides JSON API endpoints for WebBlock components.
 *
 * WebBlock API routes serve as the backend API layer for WebBlock components,
 * handling CRUD operations, form submissions, and AJAX requests that blocks need
 * to perform their functionality.
 *
 * Key differences from @Api:
 * - @WebBlockApi: APIs for WebBlock components (e.g., update name form in profile block)
 * - @Api: Standalone API endpoints (e.g., REST API for external consumption)
 *
 * Path collision prevention:
 * - WebBlock APIs use /web-block-api/ prefix to avoid collisions with /api/ routes
 * - This ensures block APIs don't interfere with standalone API endpoints
 *
 * Path generation follows file system structure with class name:
 * - /pages/profile/blocks/name_block/UpdateNameRoute.kt → /web-block-api/profile/blocks/name-block/update-name
 * - /pages/dashboard/widgets/weather/RefreshWeatherRoute.kt → /web-block-api/dashboard/widgets/weather/refresh-weather
 * - PascalCase class names become kebab-case, "Route" suffix removed
 *
 * Example - Name update API for profile block:
 * @WebBlockApi
 * class UpdateNameRoute : IApiRoute {
 *     override suspend fun getResponse(): WebBlockResponse = json(mapOf("success" to true))
 * }
 *
 * This would handle AJAX requests from a name update form within a profile block.
 *
 * Written by Claude Sonnet 4
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Suppress("unused")
annotation class WebBlockApi
