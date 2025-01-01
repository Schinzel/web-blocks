package io.schinzel.web.response_handlers

import kotlin.reflect.KClass


interface IApiEndpointResponseHandler : IResponseHandler

class ApiEndpointResponseHandlerDescriptor(
    private val endpointPackage: String
) : IResponseHandlerDescriptor<IApiEndpointResponseHandler> {
    override fun getPath(clazz: KClass<out IApiEndpointResponseHandler>): String {
        val relativePath = ResponseHandlerUtil.getRelativePath(endpointPackage, clazz)
        val classNameKebabCase = ResponseHandlerUtil.getClassNameAsKebabCase(clazz)
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName() = "ApiEndpoint"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.JSON
}