package io.schinzel.web_app_engine.route_registry.processors

import io.schinzel.web_app_engine.route_registry.IEndpointPath
import io.schinzel.web_app_engine.route_registry.getClassNameAsKebabCase
import io.schinzel.web_app_engine.route_registry.getRelativePath
import kotlin.reflect.KClass

interface IApiEndpoint : IEndpoint {
    override fun getReturnType() = ReturnTypeEnum.JSON
}

class ApiEndpointPath : IEndpointPath<IApiEndpoint> {
    override fun getPath(endpointPackage: String, clazz: KClass<out IApiEndpoint>): String {
        val relativePath = getRelativePath(endpointPackage, clazz)
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName() = "ApiEndpoint"
}