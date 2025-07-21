package io.schinzel.sample

import io.schinzel.web_blocks.test_utils.PortUtil
import io.schinzel.web_blocks.web.AbstractWebApp
import io.schinzel.web_blocks.web.request_handler.log.NoLogger
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorRegistry
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.junit.jupiter.api.*

class MyWebAppPageTest(testPort: Int) : AbstractWebApp() {
    override val logger = NoLogger()
    override val port: Int = testPort
}

/**
 * The purpose of this class is to test all sample page routes, page blocks, and page block APIs
 * to ensure they work correctly after refactoring.
 *
 * Written by Claude Sonnet 4
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SamplePageTest {
    companion object {
        private lateinit var baseUrl: String
        private lateinit var app: MyWebAppPageTest
    }

    @BeforeAll
    fun startApplication() {
        val port = PortUtil.findAvailablePort()
        baseUrl = "http://127.0.0.1:$port"
        app = MyWebAppPageTest(port)
        Thread {
            app.start()
        }.start()
        Thread.sleep(2000) // Wait for server to start
    }

    @AfterAll
    fun stopApplication() {
        app.stop()
        RouteDescriptorRegistry.clear()
    }

    @Nested
    inner class SimplePage {
        @Test
        fun returnsHelloWorldContent() {
            val response = Jsoup.connect("$baseUrl/simple-page").execute()
            val expectedContent = "Hello World"
            assertThat(response.body()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$baseUrl/simple-page").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }

    @Nested
    inner class LandingPage {
        @Test
        fun returnsLandingPageContent() {
            val response = Jsoup.connect("$baseUrl/").execute()
            val expectedContent = "Landing Page"
            assertThat(response.body()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$baseUrl/").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }

    @Nested
    inner class PageWithBlock {
        @Test
        fun containsGreetingBlockContent() {
            val response = Jsoup.connect("$baseUrl/page-with-block?user-id=123222").execute()
            val expectedContent = "greeting"
            assertThat(response.body().lowercase()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$baseUrl/page-with-block?user-id=123222").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }

        @Nested
        inner class GreetingBlock {
            @Test
            fun returnsGreetingContent() {
                val response = Jsoup.connect("$baseUrl/page-block/page-with-block/greeting-block/greeting?user-id=123222").execute()
                val expectedContent = "hi pelle"
                assertThat(response.body().lowercase()).contains(expectedContent)
            }

            @Test
            fun returns200Status() {
                val response = Jsoup.connect("$baseUrl/page-block/page-with-block/greeting-block/greeting?user-id=123222").execute()
                assertThat(response.statusCode()).isEqualTo(200)
            }
        }
    }

    @Nested
    inner class PageWithBlocksAndPageApiRoute {
        @Test
        fun containsWelcomeContent() {
            val response = Jsoup.connect("$baseUrl/page-with-blocks-and-page-api-route?user-id=123222").execute()
            val expectedContent = "welcome"
            assertThat(response.body().lowercase()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$baseUrl/page-with-blocks-and-page-api-route?user-id=123222").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }

        @Nested
        inner class UpdateNamePageBlock {
            @Test
            fun returnsFormContent() {
                val response = Jsoup.connect("$baseUrl/page-block/page-with-blocks-and-page-api-route/blocks/update-name-block/update-name?user-id=123222").execute()
                val expectedContent = "form"
                assertThat(response.body().lowercase()).contains(expectedContent)
            }

            @Test
            fun returns200Status() {
                val response = Jsoup.connect("$baseUrl/page-block/page-with-blocks-and-page-api-route/blocks/update-name-block/update-name?user-id=123222").execute()
                assertThat(response.statusCode()).isEqualTo(200)
            }
        }

        @Nested
        inner class IntroductionTextBlock {
            @Test
            fun returnsIntroContent() {
                val response = Jsoup.connect("$baseUrl/page-block/page-with-blocks-and-page-api-route/blocks/intro-text/introduction-text?user-id=123222").execute()
                val expectedContent = "introduction"
                assertThat(response.body().lowercase()).contains(expectedContent)
            }

            @Test
            fun returns200Status() {
                val response = Jsoup.connect("$baseUrl/page-block/page-with-blocks-and-page-api-route/blocks/intro-text/introduction-text?user-id=123222").execute()
                assertThat(response.statusCode()).isEqualTo(200)
            }
        }

        @Nested
        inner class WelcomeBlock {
            @Test
            fun returnsWelcomeContent() {
                val response = Jsoup.connect("$baseUrl/page-block/page-with-blocks-and-page-api-route/blocks/welcome-block/welcome?user-id=123222").execute()
                val expectedContent = "welcome"
                assertThat(response.body().lowercase()).contains(expectedContent)
            }

            @Test
            fun returns200Status() {
                val response = Jsoup.connect("$baseUrl/page-block/page-with-blocks-and-page-api-route/blocks/welcome-block/welcome?user-id=123222").execute()
                assertThat(response.statusCode()).isEqualTo(200)
            }
        }

        @Nested
        inner class UpdateFirstNamePageBlockApi {
            @Test
            fun returnsSuccessJson() {
                val response =
                    Jsoup
                        .connect("$baseUrl/page-block-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name?user-id=123222&first-name=John")
                        .ignoreContentType(true)
                        .execute()
                val expectedContent = "success"
                assertThat(response.body().lowercase()).contains(expectedContent)
            }

            @Test
            fun returns200Status() {
                val response =
                    Jsoup
                        .connect("$baseUrl/page-block-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name?user-id=123222&first-name=John")
                        .ignoreContentType(true)
                        .execute()
                assertThat(response.statusCode()).isEqualTo(200)
            }
        }
    }

    @Nested
    inner class PageWithCustomStatus {
        @Test
        fun returns201Status() {
            val response = Jsoup.connect("$baseUrl/page-with-custom-status").execute()
            assertThat(response.statusCode()).isEqualTo(201)
        }

        @Test
        fun containsStatusContent() {
            val response = Jsoup.connect("$baseUrl/page-with-custom-status").execute()
            val expectedContent = "custom status"
            assertThat(response.body().lowercase()).contains(expectedContent)
        }
    }

    @Nested
    inner class PageWithHeaders {
        @Test
        fun containsCustomHeaders() {
            val response = Jsoup.connect("$baseUrl/page-with-headers").execute()
            assertThat(response.header("X-Page-Type")).isNotNull()
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$baseUrl/page-with-headers").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }

    @Nested
    inner class JavaStylePage {
        @Test
        fun returnsJavaStyleContent() {
            val response = Jsoup.connect("$baseUrl/java-style-page").execute()
            val expectedContent = "java"
            assertThat(response.body().lowercase()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$baseUrl/java-style-page").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }
}
