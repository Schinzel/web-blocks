package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.JsonSuccessResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.Page
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi
import kotlin.reflect.KClass

/**
 * The purpose of this object is to provide utilities for detecting and validating
 * WebBlock route annotations on classes.
 *
 * Written by Claude Sonnet 4
 */
object RouteAnnotationUtil {
    /**
     * Detect which WebBlock route annotation
     * is present on a given class.
     *
     * @param clazz The class to check for route annotations
     * @return The route annotation type, or UNKNOWN if no valid annotation found
     * @throws IllegalArgumentException if multiple route annotations are present
     */
    fun detectRouteType(clazz: KClass<*>): RouteTypeEnum {
        val hasPage = clazz.annotations.any { it is Page }
        val hasApi = clazz.annotations.any { it is Api }
        val hasPageApi = clazz.annotations.any { it is WebBlockPageApi }

        val annotationCount = listOf(hasPage, hasApi, hasPageApi).count { it }

        return when {
            annotationCount == 0 -> RouteTypeEnum.UNKNOWN
            annotationCount > 1 -> throw IllegalArgumentException(
                "Class ${clazz.simpleName} has multiple route annotations. " +
                    "Only one of @WebBlockPage, @WebBlockApi, or @WebBlockPageApi is allowed.",
            )
            hasPage -> RouteTypeEnum.PAGE
            hasApi -> RouteTypeEnum.API
            hasPageApi -> RouteTypeEnum.PAGE_API
            else -> RouteTypeEnum.UNKNOWN
        }
    }

    /**
     * Validate that a class implementing
     * IWebBlockRoute has exactly one valid route annotation.
     *
     * @param clazz The class to validate
     * @throws IllegalArgumentException if validation fails
     */
    fun validateRouteAnnotation(clazz: KClass<out IWebBlockRoute>) {
        val routeType = detectRouteType(clazz)

        if (routeType == RouteTypeEnum.UNKNOWN) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} implements IWebBlockRoute but has no route annotation. " +
                    "Add @WebBlockPage, @WebBlockApi, or @WebBlockPageApi annotation.",
            )
        }
    }
}

/**
 * The purpose of this enum is to represent the different types of WebBlock routes.
 *
 * Written by Claude Sonnet 4
 */
enum class RouteTypeEnum {
    PAGE,
    API,
    PAGE_API,
    UNKNOWN,
    ;

    /**
     * The Content-Type header value for this route type.
     */
    val contentType: String
        get() =
            when (this) {
                PAGE -> "text/html"
                API -> "application/json"
                PAGE_API -> "application/json"
                UNKNOWN -> "application/octet-stream"
            }

    /**
     * Check if the given response type is valid for this route type.
     *
     * @param response The response to validate
     * @return true if the response type is valid for this route type
     */
    fun isValidResponseType(response: IWebBlockResponse): Boolean =
        when (this) {
            PAGE -> response is HtmlContentResponse
            API -> response is JsonSuccessResponse
            PAGE_API -> response is JsonSuccessResponse
            UNKNOWN -> false
        }

    /**
     * Get the expected response type name for this route type.
     *
     * @return The expected response type name
     */
    fun getExpectedResponseType(): String =
        when (this) {
            PAGE -> "HtmlResponse"
            API -> "JsonResponse"
            PAGE_API -> "JsonResponse"
            UNKNOWN -> "UnknownResponse"
        }
}
