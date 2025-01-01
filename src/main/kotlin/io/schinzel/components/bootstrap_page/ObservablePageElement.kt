package io.schinzel.components.bootstrap_page

import dev.turingcomplete.textcaseconverter.StandardTextCases
import io.schinzel.basicutils.RandomUtil
import io.schinzel.web.request_handler.log.JsonMapper
import io.schinzel.web.response_handlers.IPageEndpointResponseHandler
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * The purpose of this class is to allow page elements to be observable.
 *
 * This means that on the client side a page element can notify its
 * observers (other page elements).
 *
 * Each page element has a path a constructor arguments so that these
 * can be used or make a request to the server to get an updated version
 * of the HTML and JavaScript of the page element.
 *
 * Each page element implements the function getResponse() which returns
 * the HTML and JavaScript of the page element.
 */
abstract class ObservablePageElement : IPageEndpointResponseHandler, IPageElement {
    private val guid: String = RandomUtil.getRandomString(15)
    private val observers: MutableList<ObservablePageElement> = mutableListOf()

    /**
     * Adds an observer to the list of observers.
     */
    fun addObserver(observer: ObservablePageElement): ObservablePageElement {
        observers.add(observer)
        return this
    }

    /**
     * Returns the HTML of the page element, which is the getResponse() method
     * wrapped in a div element with several data attributes.
     */
    override fun getHtml(): String {
        // Get the HTML of the page element
        val pageElementHtml = this.getResponse()
        // Get the id of the observers
        val observersIdsAsString: String = observers.joinToString(",") { it.guid }
        // Get the path of the page element
        val pageElementEndPointPath = this.getPath()
        // Get the arguments of the page element (i.e. parameters with values)
        val constructorArguments = JsonMapper.noIndentMapper
            .writeValueAsString(this.getConstructorArguments())
            .replace("\"", "&quot;")
        return "<div id=\"$guid\" " +
                // Mark the div as a page element so that all page elements on the page are easily found
                "data-page-element " +
                // Add the ids of the observers of this page element
                "data-observer-ids=\"$observersIdsAsString\" " +
                // The path to the endpoint of this page element
                "data-path=\"$pageElementEndPointPath\" " +
                // The constructor arguments of this page element
                "data-arguments=\"$constructorArguments\"" +
                ">\n" +
                pageElementHtml +
                "</div>"
    }

    @Suppress("UNCHECKED_CAST")
    private fun getConstructorArguments(): Map<String, Any> {
        // Get the constructor parameters
        val constructorParams = this::class.primaryConstructor?.parameters ?: emptyList()
        // Get the names of the constructor parameters
        val constructorParamNames = constructorParams.map { it.name }.toSet()
        // Get the values of the constructor parameters
        return this::class.memberProperties
            .filter { it.name in constructorParamNames }
            .associate { prop ->
                val property = prop as KProperty1<ObservablePageElement, *>
                val parameterNameInKebabCase = StandardTextCases.SOFT_CAMEL_CASE
                    .convertTo(StandardTextCases.KEBAB_CASE, prop.name)
                parameterNameInKebabCase to (property.get(this) ?: throw IllegalStateException("Property ${prop.name} is null"))
            }
    }
}