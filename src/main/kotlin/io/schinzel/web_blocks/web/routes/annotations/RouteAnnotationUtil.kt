package io.schinzel.web_blocks.web.routes.annotations

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
}

