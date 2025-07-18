package io.schinzel.web_blocks.web.routes.annotations

/**
 * The purpose of this annotation is to mark a class as a WebBlocks page API route
 * that provides JSON API endpoints for page blocks (components).
 * 
 * Page API routes serve as the API layer for blocks (components) that belong to pages.
 * A page can have zero, one, or several blocks, and some blocks need API endpoints
 * to handle operations like updating data, form submissions, or AJAX requests.
 * 
 * Key differences from @WebBlockApi:
 * - @WebBlockPageApi: APIs for page blocks/components (e.g., name update form in user profile)
 * - @WebBlockApi: Standalone API endpoints (e.g., REST API for external consumption)
 * 
 * Path collision prevention:
 * - Page block APIs use /page-api/ prefix to avoid collisions with /api/ routes
 * - This ensures block APIs don't interfere with standalone API endpoints
 * 
 * Path generation follows file system structure with class name:
 * - /pages/settings/SaveNameRoute.kt → /page-api/settings/save-name
 * - /pages/user_profile/UpdateEmailRoute.kt → /page-api/user-profile/update-email
 * - PascalCase class names become kebab-case, "Route" suffix removed
 * 
 * Example - User profile page with name update block:
 * @WebBlockPageApi
 * class SaveNameRoute : WebBlockRoute {
 *     override suspend fun getResponse(): WebBlockResponse = json(mapOf("success" to true))
 * }
 * 
 * This would handle AJAX requests from a name update form block on the user profile page.
 * 
 * Written by Claude Sonnet 4
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Suppress("unused")
annotation class WebBlockPageApi