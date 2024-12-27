package io.schinzel.web_app_engine.response_handlers.response_handlers

import io.schinzel.web_app_engine.response_handlers.getClassNameAsKebabCase
import io.schinzel.web_app_engine.response_handlers.getRelativePath
import kotlin.reflect.KClass


interface IApiEndpointResponseHandler : IResponseHandler {
    override fun getReturnType() = ReturnTypeEnum.JSON
}

class ApiEndpointResponseHandlerDescriptor : IResponseHandlerDescriptor<IApiEndpointResponseHandler> {
    override fun getPath(endpointPackage: String, clazz: KClass<out IApiEndpointResponseHandler>): String {
        val relativePath = getRelativePath(endpointPackage, clazz)
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName() = "ApiEndpoint"
}