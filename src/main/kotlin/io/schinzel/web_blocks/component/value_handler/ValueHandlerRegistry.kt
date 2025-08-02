package io.schinzel.web_blocks.component.value_handler

import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.html
import java.util.concurrent.ConcurrentHashMap

/**
 * The purpose of this class it to hold a set of value handlers.
 */
class ValueHandlerRegistry {
    private val valueHandlers = ConcurrentHashMap<String, IValueHandler<*>>()

    /**
     * Register a value handler with the registry
     */
    fun <T> register(valueHandlerId: String, valueHandler: IValueHandler<T>) {
        val previous = valueHandlers.putIfAbsent(valueHandlerId, valueHandler)
        require(previous == null) { "'$valueHandlerId' already registered" }
    }

    /**
     * Register a data saver value handler
     */
    fun <T> registerSavingHandler(
        valueHandlerId: String,
        saveFunc: suspend (T) -> HtmlContentResponse,
        validateFunc: suspend (T) -> HtmlContentResponse = {
            html("<div class='success-message'>✅ Success</div>")
        }
    ) {
        // Create an anonymous ISavingValueHandler that uses the validate and save
        val handler = object : ISavingValueHandler<T> {
            override suspend fun validate(data: T) = validateFunc(data)
            override suspend fun save(data: T) = saveFunc(data)
        }
        // Register using the main register method
        register(valueHandlerId, handler)
    }


    /**
     * @throws ClassCastException at runtime if handler called with wrong data type
     * Caller responsible for ensuring type consistency
     */
    fun <T> get(valueHandlerId: String): IValueHandler<T> {
        val handler = valueHandlers[valueHandlerId]
            ?: throw ValueHandlerNotFoundException("ValueHandlerRegistry has no value handler with id '$valueHandlerId'.")

        @Suppress("UNCHECKED_CAST") return handler as IValueHandler<T>
    }


    companion object {
        // Singleton instance
        @JvmStatic
        val instance = ValueHandlerRegistry()
    }
}

