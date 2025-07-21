package io.schinzel.web_blocks.web

import io.schinzel.web_blocks.web.test_routes.MyWebApp1
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InitWebAppTest {
    private lateinit var myWebApp: MyWebApp1
    private var randomPort = -1

    @BeforeAll
    fun beforeAll() {
        myWebApp = MyWebApp1()
        randomPort = myWebApp.port
        myWebApp.start()
    }

    @AfterAll
    fun afterAll() {
        myWebApp.stop()
    }

    @Nested
    inner class Ping {
        @Test
        fun `get request _ returns pong`() {
            val actual =
                Jsoup
                    .connect("http://localhost:$randomPort/ping")
                    .ignoreContentType(true)
                    .execute()
            assertThat(actual.body()).contains("pong")
        }
    }

    @Nested
    inner class Pages {
        @Test
        fun `simple page _ get request _ contains hello world`() {
            val actual =
                Jsoup
                    .connect("http://localhost:$randomPort/simple-page")
                    .ignoreContentType(true)
                    .execute()
            assertThat(actual.body()).contains("<h1>Hello world!</h1>")
        }

        @Test
        fun `landing page base url _ get request _ contains landing page`() {
            val actual =
                Jsoup
                    .connect("http://localhost:$randomPort/")
                    .ignoreContentType(true)
                    .execute()
            assertThat(actual.body()).contains("<h1>Hello landing page!</h1>")
        }

        @Test
        fun `landing page url _ get request _ returns 404`() {
            try {
                Jsoup
                    .connect("http://localhost:$randomPort/landing")
                    .execute()
                fail("Should have thrown an exception")
            } catch (e: HttpStatusException) {
                assertThat(e.statusCode).isEqualTo(404)
            }
        }

        @Test
        fun `page in sub dirs _ get request _ contains hello world`() {
            val actual =
                Jsoup
                    .connect("http://localhost:$randomPort/page-in-dirs/my-sub-dir-1/my-sub-dir-2")
                    .ignoreContentType(true)
                    .execute()
            assertThat(actual.body()).contains("<h1>Hello sub dir world!</h1>")
        }

        @Test
        fun `page with arguments _ get request _ contains arguments`() {
            val url =
                "http://localhost:$randomPort/page-with-arguments?" +
                    "my-int=1&" +
                    "my-string=hello&" +
                    "my-boolean=true"
            val actual =
                Jsoup
                    .connect(url)
                    .ignoreContentType(true)
                    .execute()
            assertThat(actual.body())
                .contains("<p>myInt: 1</p>")
                .contains("<p>myString: hello</p>")
                .contains("<p>myBoolean: true</p>")
        }
    }
}
