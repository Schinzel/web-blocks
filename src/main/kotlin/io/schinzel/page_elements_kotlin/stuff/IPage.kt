package io.schinzel.page_elements_kotlin.stuff

interface IPage : IResponse {
    fun getHtml(): String
}

interface IApi : IResponse {
    fun getData(): Any
}

interface IResponse