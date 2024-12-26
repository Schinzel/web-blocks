package io.schinzel.pages

interface IPageElement {
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

interface ObservablePageElement : IPageElement, IObserverAndSubject {
    override val guid: String

    fun getSubscribeHtml(): String {
        val pageElementHtml = this.getHtml()
        val observersAsString = observers.joinToString(",")
        return "<div id='$guid' data-observer-ids='$observersAsString'>$pageElementHtml</div>"
    }
}

