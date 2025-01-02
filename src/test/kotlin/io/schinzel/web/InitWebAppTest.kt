package io.schinzel.web

import io.schinzel.web.request_handler.log.NoLogger
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class InitWebAppTest {
    companion object {
        val randomPort = (49152..65535).random()

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            InitWebApp(
                WebAppConfig(
                    routesPackage =  "io.schinzel.web",
                    port = randomPort,
                    logger = NoLogger(),
                    prettyFormatHtml = false
                )
            )
        }
    }

    @Test
    fun `ping - contains pong`() {
        val actual = Jsoup
            .connect("http://localhost:$randomPort/ping")
            .ignoreContentType(true)
            .execute()
        assertThat(actual.body()).contains("pong")
    }

    @Test
    fun `simple web page - contains hello world`() {
        val actual = Jsoup
            .connect("http://localhost:$randomPort/simple-page")
            .ignoreContentType(true)
            .execute()
        assertThat(actual.body()).contains("<h1>Hello world!</h1>")
    }

    @Test
    fun `landing page base url - contains Landing Page`() {
        val actual = Jsoup
            .connect("http://localhost:$randomPort/")
            .ignoreContentType(true)
            .execute()
        assertThat(actual.body()).contains("<h1>Hello landing page!</h1>")
    }

    @Test
    fun `landing page url landing - 404`() {
        try {
            Jsoup.connect("http://localhost:$randomPort/landing")
                .execute()
            fail("Should have thrown an exception")
        } catch (e: HttpStatusException) {
            assertThat(e.statusCode).isEqualTo(404)
        }
    }

    @Test
    fun `page in sub dirs - contains hello world`() {
        val actual = Jsoup
            .connect("http://localhost:$randomPort/page-in-dirs/my-sub-dir-1/my-sub-dir-2")
            .ignoreContentType(true)
            .execute()
        assertThat(actual.body()).contains("<h1>Hello sub dir world!</h1>")
    }

    @Test
    fun `page with arguments - contains arguments`() {
        val url = "http://localhost:$randomPort/page-with-arguments?" +
                "my-int=1&" +
                "my-string=hello&" +
                "my-boolean=true"
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