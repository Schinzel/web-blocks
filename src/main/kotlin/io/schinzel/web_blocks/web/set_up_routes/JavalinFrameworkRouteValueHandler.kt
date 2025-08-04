package io.schinzel.web_blocks.web.set_up_routes

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.bodyAsClass
import io.schinzel.web_blocks.component.value_handler.IValueHandler
import io.schinzel.web_blocks.component.value_handler.ValueHandlerNotFoundException
import io.schinzel.web_blocks.component.value_handler.ValueHandlerRegistry
import io.schinzel.web_blocks.web.response.HtmlContentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import java.lang.reflect.ParameterizedType

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
    val value: Any,
    val contextJson: String, // Context as JSON string from form data
)


// Get the value handler request
private fun getRequest(ctx: Context): ValueHandlerRequest = when (ctx.method().name) {
    "GET" -> {
        val valueHandlerId = ctx.queryParam("id")
            ?: throw IllegalArgumentException("Missing 'valueHandlerId' parameter")
        val value = ctx.queryParam("value")
            ?: throw IllegalArgumentException("Missing 'value' parameter")
        val context = ctx.queryParam("context")
            ?: throw IllegalArgumentException("Missing 'context' parameter")
        ValueHandlerRequest(valueHandlerId, value, context)
    }

    "POST" -> {
        // Check content type to determine how to parse
        val contentType = ctx.contentType()
        
        if (contentType?.contains("application/x-www-form-urlencoded") == true) {
            // HTMX sends form-encoded data
            val valueHandlerId = ctx.formParam("valueHandlerId")
                ?: throw IllegalArgumentException("Missing 'valueHandlerId' parameter")
            val value = ctx.formParam("value")
                ?: throw IllegalArgumentException("Missing 'value' parameter")
            val contextJson = ctx.formParam("context")
                ?: throw IllegalArgumentException("Missing 'context' parameter")
            
            ValueHandlerRequest(valueHandlerId, value, contextJson)
        } else {
            // JSON body
            ctx.bodyAsClass<ValueHandlerRequest>()
        }
    }

    else -> throw IllegalStateException("Unexpected method: ${ctx.method()}")
}

private val objectMapper = ObjectMapper()
    .registerModule(KotlinModule.Builder().build())


// Handle the value handler request
private suspend fun handleRequest(ctx: Context, valueHandlerRequest: ValueHandlerRequest) {
    try {
        // Get value-handler from registry
        val valueHandler = ValueHandlerRegistry.instance
            .get<Any, Any>(valueHandlerRequest.valueHandlerId)
        // Extract context type using reflection
        val contextType = getContextType(valueHandler)
        // Deserialize with the extracted type
        val context = objectMapper.readValue(valueHandlerRequest.contextJson, contextType)
        // Let the value-handler handle the data sent to the server
        val valueHandlerResponse: HtmlContentResponse = valueHandler
            .handle(valueHandlerRequest.value, context)
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
