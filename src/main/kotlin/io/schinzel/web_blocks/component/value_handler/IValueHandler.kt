package io.schinzel.web_blocks.component.value_handler

/**
 * The purpose of a value handler is handle data sent to the server from the client in a value
 */
interface IValueHandler<TValue, TContext> {
    /**
     * @param value The value to handle. For example: first name.
     * @param context The context of the value. For example: the user id of the value to save.
     */
    suspend fun handle(value: TValue, context: TContext): NotificationResponse
}
