package io.schinzel.pages

import io.schinzel.web_app_engine.route_registry.processors.IPageEndpointResponseHandler
import kotlin.reflect.full.primaryConstructor


interface IPageElement {
    fun getHtml(): String
}

interface IPageElement2: IPageEndpointResponseHandler {
}


interface IObserverAndSubject {
    val guid: String
    val observers: MutableList<IObserverAndSubject>

    fun addObserver(observer: IObserverAndSubject): IObserverAndSubject {
        observers.add(observer)
        return this
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

