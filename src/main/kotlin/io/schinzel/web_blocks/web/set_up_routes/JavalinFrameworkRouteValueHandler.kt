package io.schinzel.web_blocks.web.set_up_routes

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.bodyAsClass
import io.schinzel.web_blocks.component.value_handler.ValueHandlerNotFoundException
import io.schinzel.web_blocks.component.value_handler.ValueHandlerRegistry
import io.schinzel.web_blocks.component.value_handler.ValueHandlerResponse
import io.schinzel.web_blocks.component.value_handler.ValueHandlerStatus

/**
 * The purpose ot this function is to set up an endpoint for value handlers
 */
fun Javalin.setUpFrameworkRouteValueHandler(): Javalin {
    this.getAndPost("/web-blocks/value-handler") { ctx ->
        // Get the request data
        val valueHandlerRequest = getRequest(ctx)
        // Handle the request
        handleRequest(ctx, valueHandlerRequest)
    }
    // return this for chaining
    return this
}

/**
 * The purpose of the class is to represent a value handler request
 */
private data class ValueHandlerRequest(
    val id: String,
    val value: Any
)


// Get the value handler request
private fun getRequest(ctx: Context): ValueHandlerRequest = when (ctx.method().name) {
    "GET" -> {
        val id = ctx.queryParam("id")
            ?: throw IllegalArgumentException("Missing 'id' parameter")
        val value = ctx.queryParam("value")
            ?: throw IllegalArgumentException("Missing 'value' parameter")
        ValueHandlerRequest(id, value)
    }

    "POST" -> {
        ctx.bodyAsClass<ValueHandlerRequest>()
    }

    else -> throw IllegalStateException("Unexpected method: ${ctx.method()}")
}

// Handle the value handler request
private fun handleRequest(ctx: Context, valueHandlerRequest: ValueHandlerRequest) {
    try {
        val handler = ValueHandlerRegistry.instance.get<Any>(valueHandlerRequest.id)
        val valueHandlerResponse = handler.handle(valueHandlerRequest.value)
        // return the value handler response
        ctx.json(valueHandlerResponse)
    } catch (_: ValueHandlerNotFoundException) {
        ctx.status(404).json(
            ValueHandlerResponse(
                status = ValueHandlerStatus.SERVER_ERROR,
                errorMessages = listOf("Value handler '${valueHandlerRequest.id}' not found")
            )
        )
    } catch (_: ClassCastException) {
        ctx.status(400).json(
            ValueHandlerResponse(
                status = ValueHandlerStatus.VALIDATION_ERROR,
                errorMessages = listOf("Invalid data type for value handler '${valueHandlerRequest.id}'")
            )
        )
    } catch (e: Exception) {
        val errorMessage = e.message ?: "An unexpected error occurred"
        ctx.status(500).json(
            ValueHandlerResponse(
                status = ValueHandlerStatus.SERVER_ERROR,
                errorMessages = listOf(errorMessage)
            )
        )
    }
}
