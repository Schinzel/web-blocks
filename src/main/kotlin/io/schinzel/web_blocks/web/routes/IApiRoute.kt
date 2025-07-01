package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.WebBlockResponse
import kotlin.reflect.KClass


interface IApiRoute : IRoute {
    override suspend fun getResponse(): WebBlockResponse
}

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