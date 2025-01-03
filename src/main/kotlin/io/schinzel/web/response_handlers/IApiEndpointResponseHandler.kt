package io.schinzel.web.response_handlers

import kotlin.reflect.KClass


interface IApiEndpointResponseHandler : IResponseHandler

class ApiEndpointResponseHandlerDescriptor(
    private val endpointPackage: String
) : IResponseHandlerDescriptor<IApiEndpointResponseHandler> {
    override val reservedStartOfPaths: Set<String> = setOf("page", "page-api")

    override fun getRoutePath(clazz: KClass<out IApiEndpointResponseHandler>): String {
        val relativePath = ResponseHandlerUtil.getRelativePath(endpointPackage, clazz)
        val classNameKebabCase = ResponseHandlerUtil
            .removeSuffixesAndToKebabCase(clazz, listOf("Api", "Endpoint"))
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName() = "ApiEndpoint"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.JSON
}