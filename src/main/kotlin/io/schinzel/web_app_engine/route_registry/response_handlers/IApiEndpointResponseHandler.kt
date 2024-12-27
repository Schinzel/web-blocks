package io.schinzel.web_app_engine.route_registry.response_handlers

import io.schinzel.web_app_engine.route_registry.getClassNameAsKebabCase
import io.schinzel.web_app_engine.route_registry.getRelativePath
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