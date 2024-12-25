package io.schinzel.web_app_engine

import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.web_app_engine.route_registry.initializeRouteRegistry
import io.schinzel.web_app_engine.set_up_routes.setUpRoutes
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


/**
 * Test cases:
 * - landing page
 * - an api
 * - A page with several arguments with different data types
 * - A page that uses template engine.
 *
 */

class InitWebAppTest {


    @Test
    fun apa() {
        initializeRouteRegistry()
        setUpRoutes("io.schinzel.web_app_engine")

        val response = Jsoup
            .connect("http://localhost:5555/ping")
            .ignoreContentType(true)
            .execute()
        println("Response: ${response.body()}")

        Jsoup
            .connect("http://localhost:5555/test-route")
            .ignoreContentType(true)
            .execute()
            .printlnWithPrefix("Response: ")

        Jsoup
            .connect("http://localhost:5555/api/get-pets")
            .ignoreContentType(true)
            .execute()
            .printlnWithPrefix("Response: ")

        assertTrue(true)
    }

}