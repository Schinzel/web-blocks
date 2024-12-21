package io.schinzel.page_elements_kotlin.stuff

interface IPage : IResponse

interface IApi : IResponse

interface IResponse {
    fun getResponse(): String
}