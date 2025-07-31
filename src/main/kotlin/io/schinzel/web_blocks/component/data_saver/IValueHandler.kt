package io.schinzel.web_blocks.component.data_saver

import java.util.concurrent.ConcurrentHashMap

enum class ValueHandlerStatus { SUCCESS, VALIDATION_ERROR, SERVER_ERROR }

interface IValueHandlerResponse {
    val status: ValueHandlerStatus
    val errorMessages: List<String>

    val failed: Boolean get() = status != ValueHandlerStatus.SUCCESS
}

data class ValueHandlerResponse(
    override val status: ValueHandlerStatus,
    override val errorMessages: List<String> = emptyList()
) : IValueHandlerResponse


/**
 * The purpose of a value handler is handle data sent to the server from the client in a value
 */
interface IValueHandler<T> {
    fun handle(data: T): IValueHandlerResponse
}


abstract class AbstractSavingValueHandler<T> : IValueHandler<T> {
    override fun handle(data: T): IValueHandlerResponse {
        val validationResponse = validate(data)
        if (validationResponse.status != ValueHandlerStatus.SUCCESS) {
            return validationResponse
        }
        return persist(data)
    }

    protected abstract fun validate(data: T): IValueHandlerResponse
    protected abstract fun persist(data: T): IValueHandlerResponse
}


class FirstNameValueHandler : AbstractSavingValueHandler<String>() {

    override fun validate(data: String): IValueHandlerResponse {
        return ValueHandlerResponse(ValueHandlerStatus.VALIDATION_ERROR, listOf("HARDCODED ERROR"))
    }

    override fun persist(data: String): IValueHandlerResponse {
        return ValueHandlerResponse(ValueHandlerStatus.SERVER_ERROR, listOf("HARDCODED ERROR"))
    }
}


/**
 * The purpose of this class it to hold a set of value handlers.
 */
class ValueHandlerRegistry {
    private val valueHandlers = ConcurrentHashMap<String, IValueHandler<*>>()

    fun <T> register(valueHandlerName: String, valueHandler: IValueHandler<T>) {
        val previous = valueHandlers.putIfAbsent(valueHandlerName, valueHandler)
        require(previous == null) { "'$valueHandlerName' already registered" }
    }

    fun <T> register(
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


    fun <T> save(valueHandlerName: String, dataToHandle: T): IValueHandlerResponse {
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
