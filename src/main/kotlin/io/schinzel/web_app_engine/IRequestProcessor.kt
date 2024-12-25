package io.schinzel.web_app_engine

interface IWebPage : IRequestProcessor

interface IWebPageEndpoint : IRequestProcessor

interface IEndpoint : IRequestProcessor

interface IRequestProcessor {
    fun getResponse(): Any
}

