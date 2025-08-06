package io.schinzel.web_blocks.web.set_up_routes

import io.javalin.http.Context

/**
 * The purpose of this file is to provide Context extension functions
 * for Javalin request/response handling within the framework setup.
 *
 * Written by Claude Sonnet 4
 */

/**
 * Enables response preservation to prevent after-handler overrides
 */
fun Context.enablePreserveResponse() {
    this.attribute("preserveResponse", true)
}

/**
 * Checks if response preservation is enabled for this context
 */
fun Context.isPreserveResponseEnabled(): Boolean {
    return this.attribute<Boolean>("preserveResponse") == true
}
