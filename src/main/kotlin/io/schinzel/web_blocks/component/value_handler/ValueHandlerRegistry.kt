package io.schinzel.web_blocks.component.value_handler

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
        saveFunc: suspend (T) -> IValueHandlerResponse,
        validateFunc: suspend (T) -> IValueHandlerResponse = { ValueHandlerResponse(ValueHandlerStatus.SUCCESS, emptyList()) }
    ) {
        // Create an anonymous IValueHandler that combines validate and save
        val handler = object : IValueHandler<T> {
            override suspend fun handle(data: T): IValueHandlerResponse {
                // First validate
                val validationResponse = validateFunc(data)
                // If validation fails, return immediately
                if (validationResponse.failed) return validationResponse
                // Validation passed, proceed with save
                return saveFunc(data)
            }
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

        @Suppress("UNCHECKED_CAST")
        return handler as IValueHandler<T>
    }


    companion object {
        // Singleton instance
        @JvmStatic
        val instance = ValueHandlerRegistry()
    }
}


class ValueHandlerNotFoundException(override val message: String) : RuntimeException(message)
