package io.schinzel.sample2.pages.user_account.name_pe

import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.basicutils.RandomUtil
import io.schinzel.web_app_engine.request_handler.log.JsonMapper
import io.schinzel.web_app_engine.response_handlers.initializeResponseHandlerDescriptorRegistry
import io.schinzel.web_app_engine.response_handlers.response_handlers.IPageEndpointResponseHandler
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


interface IPageElement {
    fun getHtml(): String
}

interface IPageElement2 {
    @Suppress("UNCHECKED_CAST")
    fun getConstructorArguments(): Map<String, Any> {
        val clazz = this::class
        val constructorParams = clazz.primaryConstructor?.parameters ?: emptyList()
        val constructorParamNames = constructorParams.map { it.name }.toSet()

        return clazz.memberProperties
            .filter { it.name in constructorParamNames }
            .associate { prop ->
                val property = prop as KProperty1<IPageElement2, *>
                prop.name to (property.get(this) ?: throw IllegalStateException("Property ${prop.name} is null"))
            }
    }
}


interface IObserverAndSubject {
    val guid: String
    val observers: MutableList<IObserverAndSubject>

    fun addObserver(observer: IObserverAndSubject): IObserverAndSubject {
        observers.add(observer)
        return this
    }


}


interface ObservablePageElement : IPageEndpointResponseHandler, IObserverAndSubject {
    override val guid: String

    fun bapp() {
        this::class.primaryConstructor
    }

    fun getSubscribeHtml(): String {
        val pageElementHtml = this.getResponse()
        val observersAsString: String = observers.joinToString(",") { it.guid }
        val path = this.getPath()
        val arguments = this.getConstructorArguments()
        val argumentsAsString = JsonMapper.noIndentMapper
            .writeValueAsString(arguments)
            .replace("\"", "&quot;")
        return "<div id=\"$guid\" " +
                "data-observer-ids=\"$observersAsString\" " +
                "data-path=\"$path\" " +
                "data-arguments=\"$argumentsAsString\" " +
                ">$pageElementHtml</div>"
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

    override fun getResponse(): String {
        TODO("Not yet implemented")
    }

}

fun main() {

    initializeResponseHandlerDescriptorRegistry("io.schinzel.sample2")
    val myClass = MyClass("123", "456")
    myClass.getConstructorArguments().printlnWithPrefix("Arguments")
    myClass.getPath().printlnWithPrefix("Path")
    myClass.getPageElementPath().printlnWithPrefix("getPageElementPath")
}

