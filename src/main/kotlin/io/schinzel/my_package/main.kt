package io.schinzel.my_package

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.my_package.web_server.WebServer

/**
 * http://127.0.0.1:5555/
 * http://127.0.0.1:5555/index.html
 * http://127.0.0.1:5555/api/v1/myEndpoint
 * http://127.0.0.1:5555/myOtherEndpoint
 * http://127.0.0.1:5555/api/second?name=Henrik&age=52
 */
fun main() {
    WebServer()
    "*".repeat(30).println()
    "Web server started.".println()
    "*".repeat(30).println()
}