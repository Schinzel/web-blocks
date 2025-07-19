package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.WebBlockResponse

/**
 * The purpose of this interface is to define the contract for all web routes
 * in the WebBlocks framework, regardless of whether they return HTML or JSON.
 *
 * This unified interface replaces the separate IPageRoute, IApiRoute, and IPageApiRoute
 * interfaces. Route type is now determined by annotations (@WebBlockPage, @WebBlockApi,
 * @WebBlockPageApi) rather than interface inheritance.
 *
 * Route path generation follows file system structure:
 * - @WebBlockPage: /pages/simple_page/ThePage.kt → /simple-page
 * - @WebBlockApi: /api/UserPets.kt → /api/user-pets
 * - @WebBlockPageApi: /pages/settings/SaveNameRoute.kt → /page-api/settings/save-name
 *
 * Example usage:
 * @WebBlockPage
 * class ThePage : IWebBlockRoute {
 *     override suspend fun getResponse(): WebBlockResponse = html("<h1>Hello</h1>")
 * }
 *
 * @WebBlockApi
 * class UserPets : IWebBlockRoute {
 *     override suspend fun getResponse(): WebBlockResponse = json(listOf("cat", "dog"))
 * }
 *
 * Written by Claude Sonnet 4
 */

interface IWebBlockRoute : IRoute {
    /**
     * Generate the response content for this route.
     *
     * The response type (HTML or JSON) and Content-Type headers are determined by
     * the route's annotation:
     * - @WebBlockPage → HtmlResponse with text/html Content-Type
     * - @WebBlockApi → JsonResponse with application/json Content-Type
     * - @WebBlockPageApi → JsonResponse with application/json Content-Type
     *
     * @return WebBlockResponse containing the route's response data, status code, and headers
     */
    override suspend fun getResponse(): WebBlockResponse
}
