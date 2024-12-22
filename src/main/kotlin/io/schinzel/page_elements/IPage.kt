package io.schinzel.page_elements

interface IPage : IWebResponse {
    fun getHtml(): String
}

interface IApi : IWebResponse {
    fun getData(): Any
}

interface IWebResponse