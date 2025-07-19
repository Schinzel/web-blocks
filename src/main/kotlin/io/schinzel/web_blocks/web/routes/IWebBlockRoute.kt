package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse

/**
 * The purpose of this interface is to define the generic contract for all web routes
 * in the WebBlocks framework, providing type-safe response handling.
 *
 * This generic interface ensures compile-time type safety by binding route types
 * to their expected response types through type parameters.
 *
 * Written by Claude Opus 4
 */
interface IWebBlockRoute<T : IWebBlockResponse> : IRoute {
    /**
     * Generate the response content for this route.
     *
     * @return Response of type T, ensuring type safety at compile time
     */
    suspend fun getResponse(): T
}

/**
 * The purpose of this interface is to define the contract for HTML routes
 * that must return HTML responses (content, redirects, or errors).
 *
 * Routes annotated with @Page must implement this interface.
 *
 * Example usage:
 * @Page
 * class HomePage : IHtmlRoute {
 *     override suspend fun getResponse(): IHtmlResponse = 
 *         HtmlContentResponse("<h1>Welcome</h1>")
 * }
 *
 * Written by Claude Opus 4
 */
interface IHtmlRoute : IWebBlockRoute<IHtmlResponse>

/**
 * The purpose of this interface is to define the contract for API routes
 * that must return JSON responses (success or error).
 *
 * Routes annotated with @Api or @WebBlockPageApi must implement this interface.
 *
 * Example usage:
 * @Api
 * class UserApi : IApiRoute {
 *     override suspend fun getResponse(): IJsonResponse = 
 *         JsonSuccessResponse(listOf("user1", "user2"))
 * }
 *
 * Written by Claude Opus 4
 */
interface IApiRoute : IWebBlockRoute<IJsonResponse>
