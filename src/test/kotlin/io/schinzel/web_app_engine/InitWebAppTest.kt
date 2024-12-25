package io.schinzel.web_app_engine

import io.javalin.Javalin
import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.web_app_engine.route_registry.initializeRouteRegistry
import io.schinzel.web_app_engine.set_up_routes.setUpRoutes
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test


/**
 * Test cases:
 * - landing page
 * - an api
 * - An endpoint with several arguments with different data types
 * - An endpoint in subdirs
 * - A page with several arguments with different data types
 * - A page that uses template engine.
 * - A page in a couple of sub dirs
 *
 */

class InitWebAppTest {
    var javalin: Javalin? = null

    companion object {
        private lateinit var javalin: Javalin

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            initializeRouteRegistry()
            javalin = setUpRoutes("io.schinzel.web_app_engine")
                ?: throw Exception("Javalin could not be started")
        }
    }

    @Test
    fun `ping - contains pong`() {
        val actual =Jsoup
            .connect("http://localhost:5555/ping")
            .ignoreContentType(true)
            .execute()
        assertThat(actual.body()).contains("pong")
    }

    @Test
    fun `simple web page - contains hello world`() {
        val actual =Jsoup
            .connect("http://localhost:5555/simple-page")
            .ignoreContentType(true)
            .execute()
        assertThat(actual.body()).contains("<h1>Hello world!</h1>")
    }


    @Test
    fun apa() {

        Jsoup
            .connect("http://localhost:5555/api/get-pets")
            .ignoreContentType(true)
            .execute()
            .printlnWithPrefix("Response: ")

        assertTrue(true)
    }

}