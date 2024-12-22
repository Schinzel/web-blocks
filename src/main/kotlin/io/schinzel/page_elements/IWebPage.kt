package io.schinzel.page_elements

interface IWebPage : IWebResponse

interface IApi : IWebResponse

interface IWebResponse {
    fun getResponse(): Any
}