package io.schinzel.page_elements.web_response

interface IWebPage : IWebResponse

interface IApi : IWebResponse

interface IWebResponse {
    fun getResponse(): Any
}