package io.schinzel.web_app_engine.route_registry.processors

import io.schinzel.web_app_engine.route_registry.IRouteGenerator
import io.schinzel.web_app_engine.route_registry.ReturnTypeEnum
import io.schinzel.web_app_engine.route_registry.getClassNameAsKebabCase
import kotlin.reflect.KClass

interface IEndpoint : IRequestProcessor

class EndpointRouteGenerator : IRouteGenerator<IEndpoint> {
    override fun getPath(relativePath: String, clazz: KClass<out IEndpoint>): String {
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName() = "EndPoint"
    override fun getReturnType() = ReturnTypeEnum.JSON
}