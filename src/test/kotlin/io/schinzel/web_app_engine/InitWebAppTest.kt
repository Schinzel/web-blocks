package io.schinzel.web_app_engine

import io.javalin.Javalin
import io.schinzel.web_app_engine.route_registry.initializeRouteRegistry
import io.schinzel.web_app_engine.set_up_routes.setUpRoutes
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class InitWebAppTest {

    companion object {
        private lateinit var javalin: Javalin

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            initializeRouteRegistry()
            setUpRoutes("io.schinzel.web_app_engine")
        }
    }

    @Test
    fun `ping - contains pong`() {
        val actual = Jsoup
            .connect("http://localhost:5555/ping")
            .ignoreContentType(true)
            .execute()
        assertThat(actual.body()).contains("pong")
    }

    @Test
    fun `simple web page - contains hello world`() {
        val actual = Jsoup
            .connect("http://localhost:5555/simple-page")
            .ignoreContentType(true)
            .execute()
        assertThat(actual.body()).contains("<h1>Hello world!</h1>")
    }

    @Test
    fun `landing page base url - contains Landing Page`() {
        val actual = Jsoup
            .connect("http://localhost:5555/")
            .ignoreContentType(true)
            .execute()
        assertThat(actual.body()).contains("<h1>Hello landing page!</h1>")
    }

    @Test
    fun `landing page url landing - 404`() {
        try {
            Jsoup.connect("http://localhost:5555/landing")
                .execute()
            fail("Should have thrown an exception")
        } catch (e: HttpStatusException) {
            assertThat(e.statusCode).isEqualTo(404)
        }
    }

    @Test
    fun `page in sub dirs - contains hello world`() {
        val actual = Jsoup
            .connect("http://localhost:5555/page-in-dirs/my-sub-dir-1/my-sub-dir-2")
            .ignoreContentType(true)
            .execute()
        assertThat(actual.body()).contains("<h1>Hello sub dir world!</h1>")
    }

    @Test
    fun `page with arguments - contains arguments`() {
        val url = "http://localhost:5555/page-with-arguments?" +
                "myInt=1&" +
                "myString=hello&" +
                "myBoolean=true"
        val actual = Jsoup
            .connect(url)
            .ignoreContentType(true)
            .execute()
        assertThat(actual.body())
            .contains("<p>myInt: 1</p>")
            .contains("<p>myString: hello</p>")
            .contains("<p>myBoolean: true</p>")
    }
}