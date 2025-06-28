package io.schinzel.page_elements.web.routes

import kotlin.reflect.KClass


interface IApiRoute : IRoute

class ApiRouteDescriptor(
    private val endpointPackage: String
) : IRouteDescriptor<IApiRoute> {

    override fun getRoutePath(clazz: KClass<out IApiRoute>): String {
        val relativePath = RouteUtil.getRelativePath(endpointPackage, clazz)
        val classNameKebabCase = RouteUtil
            .removeSuffixesAndToKebabCase(clazz, listOf("Route"))
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName() = "ApiRoute"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.JSON
}