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
     * Register a dat
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


    fun <T> handle(valueHandlerName: String, dataToHandle: T): IValueHandlerResponse {
        return try {
            @Suppress("UNCHECKED_CAST")
            val valueHandler = valueHandlers[valueHandlerName] as? IValueHandler<T>
                ?: throw Exception("Value handler for name '$valueHandlerName' is not registered.")

            valueHandler.handle(dataToHandle)
        } catch (ex: Exception) {
            ValueHandlerResponse(
                ValueHandlerStatus.SERVER_ERROR,
                listOf("Error when trying to save $dataToHandle with data saver $valueHandlerName: ${ex.message}")
            )
        }
    }

    companion object {
        // Singleton instance
        @JvmStatic
        val instance = ValueHandlerRegistry()
    }
}
