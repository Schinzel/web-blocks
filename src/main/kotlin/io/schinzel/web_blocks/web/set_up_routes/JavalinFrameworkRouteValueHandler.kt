package io.schinzel.web_blocks.web.set_up_routes

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.bodyAsClass
import io.schinzel.web_blocks.component.value_handler.ValueHandlerNotFoundException
import io.schinzel.web_blocks.component.value_handler.ValueHandlerRegistry
import io.schinzel.web_blocks.web.response.HtmlContentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking

/**
 * The purpose ot this function is to set up an endpoint for value handlers
 */
fun Javalin.setUpFrameworkRouteValueHandler(): Javalin {
    this.getAndPost("/web-blocks/value-handler") { ctx ->
        runBlocking(Dispatchers.IO) {
            // Get the request data
            val valueHandlerRequest = getRequest(ctx)
            // Handle the request
            handleRequest(ctx, valueHandlerRequest)
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
private suspend fun handleRequest(ctx: Context, valueHandlerRequest: ValueHandlerRequest) {
    try {
        // Get value-handler from registry
        val valueHandler = ValueHandlerRegistry.instance
            .get<Any>(valueHandlerRequest.valueHandlerId)
        // Let the value-handler handle the data sent to the server
        val valueHandlerResponse: HtmlContentResponse = valueHandler
            .handle(valueHandlerRequest.value)
        // Set status code
        ctx.status(valueHandlerResponse.status)
        // Set custom headers if provided
        valueHandlerResponse.headers.forEach { (key, value) ->
            ctx.header(key, value)
        }
        // return the value handler response
        ctx.html(valueHandlerResponse.content)
    } catch (_: ValueHandlerNotFoundException) {
        ctx.status(404)
            .html("<div class='error-message'>Value handler '${valueHandlerRequest.valueHandlerId}' not found</div>")
    } catch (_: ClassCastException) {
        ctx.status(400)
            .html("<div class='error-message'>Invalid data type for value handler '${valueHandlerRequest.valueHandlerId}'</div>")
    } catch (e: Exception) {
        val errorMessage = e.message ?: "An unexpected error occurred"
        ctx.status(500).html("<div class='error-message'>An error occurred: '$errorMessage'</div>")
    }
}
