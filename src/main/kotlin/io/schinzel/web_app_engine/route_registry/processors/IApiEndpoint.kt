package io.schinzel.web_app_engine.route_registry.processors

import io.schinzel.web_app_engine.route_registry.IRouteGenerator
import io.schinzel.web_app_engine.route_registry.ReturnTypeEnum
import io.schinzel.web_app_engine.route_registry.getClassNameAsKebabCase
import kotlin.reflect.KClass

interface IApiEndpoint : IEndpoint

class ApiEndpointRouteGenerator : IRouteGenerator<IApiEndpoint> {
    override fun getPath(relativePath: String, clazz: KClass<out IApiEndpoint>): String {
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName() = "ApiEndpoint"
    override fun getReturnType() = ReturnTypeEnum.JSON
}