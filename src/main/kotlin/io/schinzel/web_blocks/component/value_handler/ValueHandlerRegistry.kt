package io.schinzel.web_blocks.component.value_handler

import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.html
import java.util.concurrent.ConcurrentHashMap

/**
 * The purpose of this class it to hold a set of value handlers.
 */
class ValueHandlerRegistry {
    private val valueHandlers = ConcurrentHashMap<String, IValueHandler>()

    /**
     * Register a value handler with the registry
     */
    fun register(valueHandlerId: String, valueHandler: IValueHandler) {
        val previous = valueHandlers.putIfAbsent(valueHandlerId, valueHandler)
        require(previous == null) { "'$valueHandlerId' already registered" }
    }

    /**
     * Register a data saver value handler
     */
    fun registerSavingHandler(
        valueHandlerId: String,
        saveFunc: suspend (String, Map<String, String>) -> HtmlContentResponse,
        validateFunc: suspend (String) -> HtmlContentResponse = {
            html("<div class='success-message'>✅ Success</div>")
        }
    ) {
        // Create an anonymous ISavingValueHandler that uses the validate and save
        val handler = object : ISavingValueHandler {
            override suspend fun validate(data: String) = validateFunc(data)
            override suspend fun save(data: String, parameters: Map<String, String>) = saveFunc(data, parameters)
        }
        // Register using the main register method
        register(valueHandlerId, handler)
    }

    /**
     * Get a value handler by id
     */
    fun get(valueHandlerId: String): IValueHandler {
        return valueHandlers[valueHandlerId]
            ?: throw ValueHandlerNotFoundException("ValueHandlerRegistry has no value handler with id '$valueHandlerId'.")
    }

    companion object {
        // Singleton instance
        @JvmStatic
        val instance = ValueHandlerRegistry()
    }
}
