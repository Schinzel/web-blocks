package io.schinzel.web_blocks.component.page_builder

import dev.turingcomplete.textcaseconverter.StandardTextCases
import io.schinzel.basicutils.RandomUtil
import io.schinzel.web_blocks.web.request_handler.log.JsonMapper
import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.HtmlErrorResponse
import io.schinzel.web_blocks.web.response.HtmlRedirectResponse
import io.schinzel.web_blocks.web.routes.IHtmlRoute
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * The purpose of this class is to allow blocks to be observable.
 *
 * This means that on the client side a block can notify its
 * observers (other blocks).
 *
 * Each block has a path and constructor arguments so that these
 * can be used to make a request to the server to get an updated version
 * of the HTML and JavaScript of the block.
 *
 * Each block implements the function getResponse() which returns
 * the HTML and JavaScript of the block.
 */
abstract class WebBlock :
    IHtmlRoute,
    IBlock {
    private val guid: String = RandomUtil.getRandomString(15)
    private val observers: MutableList<WebBlock> = mutableListOf()

    /**
     * Adds an observer to the list of observers.
     */
    fun addObserver(observer: WebBlock): WebBlock {
        observers.add(observer)
        return this
    }

    /**
     * Returns the HTML of the block, which is the getResponse() method
     * wrapped in a div element with several data attributes.
     */
    override suspend fun getHtml(): String {
        // Get the HTML of the block
        val response = this.getResponse()
        val blockHtml =
            when (response) {
                is HtmlContentResponse -> response.content
                is HtmlRedirectResponse -> {
                    // For redirects in blocks, we might want to handle this differently
                    // For now, just show a message
                    "<p>Redirecting to: ${response.location}</p>"
                }
                is HtmlErrorResponse -> {
                    // Display error content
                    response.content
                }
            }
        // Get the id of the observers
        val observersIdsAsString: String = observers.joinToString(",") { it.guid }
        // Get the path of the block
        val blockEndpointPath = this.getPath()
        // Get the arguments of the block (i.e. parameters with values)
        val constructorArguments =
            JsonMapper.noIndentMapper
                .writeValueAsString(this.getConstructorArguments())
                .replace("\"", "&quot;")
        return "<div id=\"$guid\" " +
            // Mark the div as a block so that all blocks on the page are easily found
            "data-block " +
            // Add the ids of the observers of this block
            "data-observer-ids=\"$observersIdsAsString\" " +
            // The path to the endpoint of this block
            "data-path=\"$blockEndpointPath\" " +
            // The constructor arguments of this block
            "data-arguments=\"$constructorArguments\"" +
            ">\n" +
            blockHtml +
            "</div>"
    }

    @Suppress("UNCHECKED_CAST")
    private fun getConstructorArguments(): Map<String, Any> {
        // Get the constructor parameters of the class that implements this class
        val constructorParams = this::class.primaryConstructor?.parameters ?: emptyList()
        // Get the names of the constructor parameters
        val constructorParamNames = constructorParams.map { it.name }.toSet()
        // Get the values of the constructor parameters
        return this::class
            .memberProperties
            // Filter out the properties that are not constructor parameters
            .filter { it.name in constructorParamNames }
            .associate { prop ->
                val property = prop as KProperty1<WebBlock, *>
                // Convert the parameter name to kebab case
                val parameterNameInKebabCase =
                    StandardTextCases.SOFT_CAMEL_CASE
                        .convertTo(StandardTextCases.KEBAB_CASE, prop.name)
                parameterNameInKebabCase to (
                    property.get(this)
                        ?: throw IllegalStateException("Property ${prop.name} is null")
                )
            }
    }
}
