package io.schinzel.pages.bootstrap_page_v2

import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.basicutils.RandomUtil
import io.schinzel.web_app_engine.request_handler.log.JsonMapper
import io.schinzel.web_app_engine.response_handlers.initializeResponseHandlerDescriptorRegistry
import io.schinzel.web_app_engine.response_handlers.response_handlers.IPageEndpointResponseHandler
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


interface IPageElementV2 {
    fun getHtml(): String
}


interface IObserverAndSubject {
    val guid: String
    val observers: MutableList<IObserverAndSubject>

    fun addObserver(observer: IObserverAndSubject): IObserverAndSubject {
        observers.add(observer)
        return this
    }
}


interface ObservablePageElement : IPageEndpointResponseHandler, IObserverAndSubject, IPageElementV2 {


    override fun getResponse(): String {
        val pageElementHtml = this.getHtml()
        val observersAsString: String = observers.joinToString(",") { it.guid }
        val path = this.getPath()
        val arguments = this.getConstructorArguments()
        val argumentsAsString = JsonMapper.noIndentMapper
            .writeValueAsString(arguments)
            .replace("\"", "&quot;")
        return "<div id=\"$guid\" " +
                "data-observer-ids=\"$observersAsString\" " +
                "data-path=\"$path\" " +
                "data-arguments=\"$argumentsAsString\"" +
                ">\n" +
                pageElementHtml +
                "</div>"
    }

    @Suppress("UNCHECKED_CAST")
    fun getConstructorArguments(): Map<String, Any> {
        val clazz = this::class
        val constructorParams = clazz.primaryConstructor?.parameters ?: emptyList()
        val constructorParamNames = constructorParams.map { it.name }.toSet()
        return clazz.memberProperties
            .filter { it.name in constructorParamNames }
            .associate { prop ->
                val property = prop as KProperty1<IObserverAndSubject, *>
                prop.name to (property.get(this) ?: throw IllegalStateException("Property ${prop.name} is null"))
            }
    }

}


class MyClass(val userId: String, val petId: String) : ObservablePageElement {
    override val guid: String = RandomUtil.getRandomString(10)
    override val observers: MutableList<IObserverAndSubject> = mutableListOf()
    private val petName: String = "Fluffy"

    override fun getHtml(): String {
        TODO("Not yet implemented")
    }
}

fun main() {

    initializeResponseHandlerDescriptorRegistry("io.schinzel.sample2")
    val myClass = MyClass("123", "456")
    myClass.getConstructorArguments().printlnWithPrefix("Arguments")
    myClass.getPath().printlnWithPrefix("Path")
}

