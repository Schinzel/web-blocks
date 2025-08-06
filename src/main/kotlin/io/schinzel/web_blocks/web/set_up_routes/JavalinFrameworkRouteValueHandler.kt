package io.schinzel.web_blocks.web.set_up_routes

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.bodyAsClass
import io.schinzel.web_blocks.component.value_handler.IValueHandler
import io.schinzel.web_blocks.component.value_handler.NotificationResponse
import io.schinzel.web_blocks.component.value_handler.ValueHandlerNotFoundException
import io.schinzel.web_blocks.component.value_handler.ValueHandlerRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import java.lang.reflect.ParameterizedType

/**
 * The purpose of this function is to set up an endpoint for value handlers
 */
fun Javalin.setUpFrameworkRouteValueHandler(): Javalin {
    this.post("/web-blocks/value-handler") { ctx ->
        runBlocking(Dispatchers.IO) {
            handleRequest(ctx)
        }
    }
    // return this for chaining
    return this
}

/**
 * The purpose of the class is to represent a value handler request
 */
private data class ValueHandlerRequest(
    val valueHandlerId: String,
    val value: Any,
    val contextJson: String,
)


private val objectMapper = ObjectMapper()
    .registerModule(KotlinModule.Builder().build())


// Handle the value handler request
private suspend fun handleRequest(ctx: Context) {
    // Parse the request data with early return on error
    val valueHandlerRequest = try {
        ctx.bodyAsClass<ValueHandlerRequest>()
    } catch (_: Exception) {
        ctx.status(400).json(buildMap {
            put("status", "error")
            put("message", "Invalid JSON request")
        })
        return
    }

    try {
        // Get value-handler from registry
        val valueHandler = ValueHandlerRegistry.instance
            .get<Any, Any>(valueHandlerRequest.valueHandlerId)
        // Extract context type using reflection
        val contextType = getContextType(valueHandler)
        // Deserialize with the extracted type
        val context = objectMapper.readValue(valueHandlerRequest.contextJson, contextType)
        // Let the value-handler handle the data sent to the server
        val valueHandlerResponse: NotificationResponse = valueHandler
            .handle(valueHandlerRequest.value, context)
        // return response from value handler
        ctx.status(valueHandlerResponse.status).json(buildMap {
            put("type", valueHandlerResponse.type)
            put("message", valueHandlerResponse.message)
            put("details", valueHandlerResponse.details)
        })
        // This sets that web blocks should not try an alter the error on the way
        ctx.enablePreserveResponse()
    } catch (_: ValueHandlerNotFoundException) {
        ctx.status(404).json(buildMap {
            put("status", "error")
            put("message", "Value handler '${valueHandlerRequest.valueHandlerId}' not found")
        })
    } catch (_: ClassCastException) {
        ctx.status(400).json(buildMap {
            put("status", "error")
            put("message", "Invalid data type for value handler '${valueHandlerRequest.valueHandlerId}'")
        })
    } catch (e: Exception) {
        val errorMessage = e.message ?: "An unexpected error occurred"
        ctx.status(500).json(buildMap {
            put("status", "error")
            put("message", "An error occurred: '$errorMessage'")
        })
    }
}


private fun <TValue, TContext> getContextType(handler: IValueHandler<TValue, TContext>): Class<*> {
    // Get the class of the handler implementation
    val handlerClass = handler::class.java

    // Find the IValueHandler interface among implemented interfaces
    val valueHandlerInterface = handlerClass.genericInterfaces
        .filterIsInstance<ParameterizedType>()
        .find { (it.rawType as Class<*>).isAssignableFrom(IValueHandler::class.java) }
        ?: throw IllegalStateException("Handler doesn't implement IValueHandler properly")

    // Extract the second type argument (TContext)
    val typeArguments = valueHandlerInterface.actualTypeArguments
    return typeArguments[1] as Class<*>  // Index 1 = TContext
}
