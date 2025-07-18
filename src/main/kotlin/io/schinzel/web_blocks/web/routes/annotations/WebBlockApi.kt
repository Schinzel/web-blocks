package io.schinzel.web_blocks.web.routes.annotations

/**
 * The purpose of this annotation is to mark a class as a WebBlocks API route
 * that returns JSON content for standalone API endpoints.
 *
 * API routes are discovered from the /api directory structure and generate
 * JSON responses with Content-Type: application/json.
 *
 * Path generation follows file system structure with class name:
 * - /api/UserPets.kt → /api/user-pets
 * - /api/user_management/UserInfo.kt → /api/user-management/user-info
 * - PascalCase class names become kebab-case, "Route" suffix removed
 *
 * Example:
 * @WebBlockApi
 * class UserPets : IWebBlockRoute {
 *     override suspend fun getResponse(): WebBlockResponse = json(listOf("cat", "dog"))
 * }
 *
 * Written by Claude Sonnet 4
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Suppress("unused")
annotation class WebBlockApi
