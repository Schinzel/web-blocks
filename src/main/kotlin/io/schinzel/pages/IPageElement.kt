package io.schinzel.pages

interface IPageElement {
    fun getHtml(): String
}

interface IObserver

interface ISubject {
    val observers: MutableList<IObserver>

    fun addObserver(observer: IObserver): ISubject {
        observers.add(observer)
        return this
    }
}

interface ObservablePageElement : IPageElement, IObserver, ISubject