package io.schinzel.web_blocks.web.set_up_routes

import io.javalin.Javalin
import io.javalin.http.Context

/**
 * The purpose of this is to register an endpoint with both get and
 * post for easier usage. When developing it can be convenient
 * to have the post-endpoint you are developing
 * available as a get-endpoint for easier debugging using a browser.
 */
fun Javalin.getAndPost(
    path: String,
    handler: (ctx: Context) -> Unit,
) {
    this.get(path, handler)
    this.post(path, handler)
}
