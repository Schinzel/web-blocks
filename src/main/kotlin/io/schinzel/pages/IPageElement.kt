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

interface ObservablePageElement : IPageElement, IObserver, ISubject {

    fun getBapp(): String {
        val pageElementHtml = this.getHtml()
        val guid = "my_guid"
        return "<div id='$guid'>$pageElementHtml</div>"
    }

}

