package io.schinzel.web_blocks.web.set_up_routes

import io.javalin.Javalin
import io.javalin.http.Context
import io.schinzel.web_blocks.component.value_handler.IValueHandler
import io.schinzel.web_blocks.component.value_handler.ValueHandlerNotFoundException
import io.schinzel.web_blocks.component.value_handler.ValueHandlerRegistry
import io.schinzel.web_blocks.web.response.HtmlContentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking

/**
 * The purpose of this function is to set up an endpoint for value handlers
 */
fun Javalin.setUpFrameworkRouteValueHandler(): Javalin {
    this.post("/web-blocks/value-handler") { ctx ->
        runBlocking(Dispatchers.IO) {
            handleValueHandlerRequest(ctx)
        }
    }
    // return this for chaining
    return this
}

// Handle the value handler request
private suspend fun handleValueHandlerRequest(ctx: Context) {
    try {
        // Extract valueHandlerId from form parameters
        val valueHandlerId = ctx.formParam("valueHandlerId")
            ?: throw IllegalArgumentException("Missing 'valueHandlerId' parameter")
        
        // Extract the input value from form parameters
        val value = ctx.formParam("value")
            ?: throw IllegalArgumentException("Missing 'value' parameter")
        
        // Build parameters map from all other form parameters
        val parameters = ctx.formParamMap()
            .filterKeys { it != "valueHandlerId" && it != "value" }
            .mapValues { it.value.firstOrNull() ?: "" }
        
        // Get value-handler from registry
        val valueHandler: IValueHandler = ValueHandlerRegistry.instance.get(valueHandlerId)
        
        // Let the value-handler handle the data sent to the server
        val valueHandlerResponse: HtmlContentResponse = valueHandler.handle(value, parameters)
        
        // Set status code
        ctx.status(valueHandlerResponse.status)
        
        // Set custom headers if provided
        valueHandlerResponse.headers.forEach { (key, headerValue) ->
            ctx.header(key, headerValue)
        }
        
        // Return the value handler response
        ctx.html(valueHandlerResponse.content)
        
    } catch (_: ValueHandlerNotFoundException) {
        ctx.status(404)
            .html("<div class='error-message'>Value handler '${ctx.formParam("valueHandlerId")}' not found</div>")
    } catch (e: IllegalArgumentException) {
        ctx.status(400)
            .html("<div class='error-message'>${e.message}</div>")
    } catch (e: Exception) {
        val errorMessage = e.message ?: "An unexpected error occurred"
        ctx.status(500)
            .html("<div class='error-message'>An error occurred: '$errorMessage'</div>")
    }
}