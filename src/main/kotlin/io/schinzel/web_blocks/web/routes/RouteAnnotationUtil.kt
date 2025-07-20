package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.Page
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import kotlin.reflect.KClass
import io.schinzel.web_blocks.web.routes.annotations.WebBlock as WebBlockAnnotation

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
        val hasWebBlock = clazz.annotations.any { it is WebBlockAnnotation }
        val hasWebBlockApi = clazz.annotations.any { it is WebBlockApi }

        val annotationCount = listOf(hasPage, hasApi, hasWebBlock, hasWebBlockApi).count { it }

        return when {
            annotationCount == 0 -> RouteTypeEnum.UNKNOWN
            annotationCount > 1 -> throw IllegalArgumentException(
                "Class ${clazz.simpleName} has multiple route annotations. " +
                    "Only one of @Page, @Api, @WebBlock, or @WebBlockApi is allowed.",
            )
            hasPage -> RouteTypeEnum.PAGE
            hasApi -> RouteTypeEnum.API
            hasWebBlock -> RouteTypeEnum.WEB_BLOCK
            hasWebBlockApi -> RouteTypeEnum.WEB_BLOCK_API
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
    fun validateRouteAnnotation(clazz: KClass<out IWebBlockRoute<*>>) {
        val routeType = detectRouteType(clazz)

        if (routeType == RouteTypeEnum.UNKNOWN) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} implements IWebBlockRoute but has no route annotation. " +
                    "Add @Page, @Api, @WebBlock, or @WebBlockApi annotation.",
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
    WEB_BLOCK,
    WEB_BLOCK_API,
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
                WEB_BLOCK -> "text/html"
                WEB_BLOCK_API -> "application/json"
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
            PAGE -> response is IHtmlResponse
            API -> response is IJsonResponse
            WEB_BLOCK -> response is IHtmlResponse
            WEB_BLOCK_API -> response is IJsonResponse
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
            WEB_BLOCK -> "HtmlResponse"
            WEB_BLOCK_API -> "JsonResponse"
            UNKNOWN -> "UnknownResponse"
        }
}
