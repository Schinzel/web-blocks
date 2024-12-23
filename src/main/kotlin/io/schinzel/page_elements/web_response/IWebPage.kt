package io.schinzel.page_elements.web_response

interface IWebPage : IRequestProcessor

interface IWebPageEndpoint : IRequestProcessor


interface IEndpoint : IRequestProcessor

interface IRequestProcessor {
    fun getResponse(): Any
}

