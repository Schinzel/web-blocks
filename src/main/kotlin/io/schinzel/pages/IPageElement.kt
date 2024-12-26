package io.schinzel.pages

interface IPageElement {
    fun getHtml(): String
}

interface IObserver {
    fun update()
}

interface ISubject {
    val observers: MutableList<IObserver>

    fun addObserver(observer: IObserver): ISubject {
        observers.add(observer)
        return this
    }

    fun notifyObservers() {
        observers.forEach { it.update() }
    }
}