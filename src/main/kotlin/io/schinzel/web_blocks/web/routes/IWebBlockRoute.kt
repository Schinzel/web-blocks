package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse

/**
 * The purpose of this interface is to define the contract for all web routes
 * in the WebBlocks framework, regardless of whether they return HTML or JSON.
 */
// Generic base interface
interface IWebBlockRoute<T : IWebBlockResponse> {
    suspend fun getResponse(): T
}

// Specific route interfaces
interface IHtmlRoute : IWebBlockRoute<IHtmlResponse>
interface IApiRoute : IWebBlockRoute<IJsonResponse>
