package io.schinzel.web_blocks.web.routes.annotations

import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
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
        val hasApi = clazz.annotations.any { it is Api }
        val hasPage = clazz.annotations.any { it is Page }
        val hasPageBlock = clazz.annotations.any { it is PageBlock }
        val hasPageBlockApi = clazz.annotations.any { it is PageBlockApi }

        val annotationCount = listOf(hasPage, hasApi, hasPageBlock, hasPageBlockApi).count { it }

        return when {
            annotationCount == 0 -> RouteTypeEnum.UNKNOWN
            annotationCount > 1 -> throw IllegalArgumentException(
                "Class ${clazz.simpleName} has multiple route annotations. " +
                    "Only one of @Api, @Page, @PageBlock, @or @PageBlockApi is allowed.",
            )
            hasPage -> RouteTypeEnum.PAGE
            hasApi -> RouteTypeEnum.API
            hasPageBlock -> RouteTypeEnum.PAGE_BLOCK
            hasPageBlockApi -> RouteTypeEnum.PAGE_BLOCK_API
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
                    "Add @Api, @Page, @PageBlock, @or @PageBlockApi annotation.",
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
    PAGE_BLOCK,
    PAGE_BLOCK_API,
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
                PAGE_BLOCK -> "text/html"
                PAGE_BLOCK_API -> "application/json"
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
            PAGE -> response is IHtmlResponse
            API -> response is IJsonResponse
            PAGE_BLOCK -> response is IHtmlResponse
            PAGE_BLOCK_API -> response is IJsonResponse
            PAGE_API -> response is IJsonResponse
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
            PAGE_BLOCK -> "HtmlResponse"
            PAGE_BLOCK_API -> "JsonResponse"
            PAGE_API -> "JsonResponse"
            UNKNOWN -> "UnknownResponse"
        }
}
