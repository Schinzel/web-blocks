package io.schinzel.pages.bootstrap_page_v2

import io.schinzel.basicutils.RandomUtil
import io.schinzel.web_app_engine.request_handler.log.JsonMapper
import io.schinzel.web_app_engine.response_handlers.response_handlers.IPageEndpointResponseHandler
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


interface IPageElementV2 {
    fun getHtml(): String
}


abstract class ObservablePageElement : IPageEndpointResponseHandler, IPageElementV2 {
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
                "data-page-element " +
                "data-observer-ids=\"$observersIdsAsString\" " +
                "data-path=\"$pageElementEndPointPath\" " +
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
                prop.name to (property.get(this) ?: throw IllegalStateException("Property ${prop.name} is null"))
            }
    }
}
