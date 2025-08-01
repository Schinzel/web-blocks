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
    fun <T> register(valueHandlerName: String, valueHandler: IValueHandler<T>) {
        val previous = valueHandlers.putIfAbsent(valueHandlerName, valueHandler)
        require(previous == null) { "'$valueHandlerName' already registered" }
    }

    /**
     * Register a data saver value handler
     */
    fun <T> registerSavingHandler(
        name: String,
        saveFunc: (T) -> IValueHandlerResponse,
        validateFunc: (T) -> IValueHandlerResponse = { ValueHandlerResponse(ValueHandlerStatus.SUCCESS, emptyList()) }
    ) {
        // Create an anonymous IValueHandler that combines validate and save
        val handler = object : IValueHandler<T> {
            override fun handle(data: T): IValueHandlerResponse {
                // First validate
                val validationResponse = validateFunc(data)
                // If validation fails, return immediately
                if (validationResponse.failed) {
                    return validationResponse
                }
                // Validation passed, proceed with save
                return saveFunc(data)
            }
        }
        // Register using the main register method
        register(name, handler)
    }


    fun <T> get(valueHandlerName: String): IValueHandler<T> {
        val handler = valueHandlers[valueHandlerName]
            ?: throw ValueHandlerNotFoundException("ValueHandlerRegistry has no value handler named '$valueHandlerName'.")

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
