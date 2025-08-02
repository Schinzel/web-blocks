package io.schinzel.web_blocks.component.value_handler

import io.schinzel.web_blocks.web.response.HtmlContentResponse


/**
 * The purpose of a value handler is handle data sent to the server from the client in a value
 */
interface IValueHandler<T> {
    suspend fun handle(data: T): HtmlContentResponse
}
