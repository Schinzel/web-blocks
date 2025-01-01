package io.schinzel.web.response_handlers.response_handlers

import io.schinzel.web.response_handlers.getClassNameAsKebabCase
import io.schinzel.web.response_handlers.getRelativePath
import kotlin.reflect.KClass


interface IApiEndpointResponseHandler : IResponseHandler

class ApiEndpointResponseHandlerDescriptor(
    private val endpointPackage: String
) : IResponseHandlerDescriptor<IApiEndpointResponseHandler> {
    override fun getPath(clazz: KClass<out IApiEndpointResponseHandler>): String {
        val relativePath = getRelativePath(endpointPackage, clazz)
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName() = "ApiEndpoint"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.JSON
}