package io.schinzel.web_blocks.component.value_handler

import io.schinzel.web_blocks.web.response.HtmlContentResponse

/**
 * The purpose of a value handler is handle data sent to the server from the client in a value
 */
interface IValueHandler {
    /**
     * @param value The value to handle. For example: first name.
     * @param parameters Additional parameters from form data. For example: user id, validation flags, etc.
     */
    suspend fun handle(value: String, parameters: Map<String, String>): HtmlContentResponse
}
