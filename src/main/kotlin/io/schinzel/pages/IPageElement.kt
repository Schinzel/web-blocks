package io.schinzel.pages

import io.schinzel.web_app_engine.route_registry.processors.IPageEndpointResponseHandler
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


interface ObservablePageElement : IPageElement, IObserverAndSubject {
    override val guid: String

    fun bapp(){
        this::class.primaryConstructor
    }

    fun getSubscribeHtml(): String {
        val pageElementHtml = this.getHtml()
        val observersAsString: String = observers.joinToString(",") { it.guid }
        return "<div id='$guid' data-observer-ids='$observersAsString'>$pageElementHtml</div>"
    }
}

