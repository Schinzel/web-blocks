package io.schinzel.web_app_engine.route_registry.response_handlers

import io.schinzel.web_app_engine.route_registry.getClassNameAsKebabCase
import io.schinzel.web_app_engine.route_registry.getRelativePath
import kotlin.reflect.KClass

interface IEndpointResponseHandler : IResponseHandler {
    override fun getReturnType() = ReturnTypeEnum.JSON
}

class EndpointResponseHandlerDescriptor : IResponseHandlerDescriptor<IEndpointResponseHandler> {
    override fun getPath(endpointPackage: String, clazz: KClass<out IEndpointResponseHandler>): String {
        val relativePath = getRelativePath(endpointPackage, clazz)
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName() = "ApiEndpoint"
}