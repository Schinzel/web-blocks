package io.schinzel.web_blocks.component.value_handler

import io.schinzel.web_blocks.web.response.IJsonResponse

/**
 * The purpose of this class is to provide a structured response for value handler
 * notifications with consistent typing and prevention of incorrect status codes
 * through private constructor and enum-based factory methods.
 *
 * Written by Claude Sonnet 4
 */
data class NotificationResponse(
    val type: NotificationResponseEnum,
    val message: String,
    val details: List<String>,
    override val status: Int
) : IJsonResponse
