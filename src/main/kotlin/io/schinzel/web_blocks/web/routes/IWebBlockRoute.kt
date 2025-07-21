package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse

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
 * The purpose of this interface is to define the contract for HTML routes,
 * i.e. routes that return HTML.
 */
interface IHtmlRoute : IWebBlockRoute<IHtmlResponse>

/**
 * The purpose of this interface is to define the contract for JSON routes,
 * i.e. routes that return JSON.
 */
interface IJsonRoute : IWebBlockRoute<IJsonResponse>
