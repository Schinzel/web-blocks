package io.schinzel.sample

import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * The purpose of this class is to test all sample page routes, page blocks, and page block APIs
 * to ensure they work correctly after refactoring.
 *
 * Written by Claude Sonnet 4
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SamplePageTest {
    companion object {
        private const val BASE_URL = "http://127.0.0.1:5555"
        private lateinit var app: MyWebApp
    }

    @BeforeAll
    fun startApplication() {
        app = MyWebApp()
        Thread {
            app.start()
        }.start()
        Thread.sleep(2000) // Wait for server to start
    }

    @AfterAll
    fun stopApplication() {
        app.stop()
    }

    @Nested
    inner class SimplePage {
        @Test
        fun returnsHelloWorldContent() {
            val response = Jsoup.connect("$BASE_URL/simple-page").execute()
            val expectedContent = "Hello World"
            assertThat(response.body()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$BASE_URL/simple-page").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }

    @Nested
    inner class LandingPage {
        @Test
        fun returnsLandingPageContent() {
            val response = Jsoup.connect("$BASE_URL/").execute()
            val expectedContent = "Landing Page"
            assertThat(response.body()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$BASE_URL/").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }

    @Nested
    inner class PageWithBlock {
        @Test
        fun containsGreetingBlockContent() {
            val response = Jsoup.connect("$BASE_URL/page-with-block?user-id=123222").execute()
            val expectedContent = "greeting"
            assertThat(response.body().lowercase()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$BASE_URL/page-with-block?user-id=123222").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }

        @Nested
        inner class GreetingBlock {
            @Test
            fun returnsGreetingContent() {
                val response = Jsoup.connect("$BASE_URL/page-block/page-with-block/greeting-block/greeting?user-id=123222").execute()
                val expectedContent = "hi pelle"
                assertThat(response.body().lowercase()).contains(expectedContent)
            }

            @Test
            fun returns200Status() {
                val response = Jsoup.connect("$BASE_URL/page-block/page-with-block/greeting-block/greeting?user-id=123222").execute()
                assertThat(response.statusCode()).isEqualTo(200)
            }
        }
    }

    @Nested
    inner class PageWithBlocksAndPageApiRoute {
        @Test
        fun containsWelcomeContent() {
            val response = Jsoup.connect("$BASE_URL/page-with-blocks-and-page-api-route?user-id=123222").execute()
            val expectedContent = "welcome"
            assertThat(response.body().lowercase()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$BASE_URL/page-with-blocks-and-page-api-route?user-id=123222").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }

        @Nested
        inner class UpdateNamePageBlock {
            @Test
            fun returnsFormContent() {
                val response = Jsoup.connect("$BASE_URL/page-block/page-with-blocks-and-page-api-route/blocks/update-name-block/update-name?user-id=123222").execute()
                val expectedContent = "form"
                assertThat(response.body().lowercase()).contains(expectedContent)
            }

            @Test
            fun returns200Status() {
                val response = Jsoup.connect("$BASE_URL/page-block/page-with-blocks-and-page-api-route/blocks/update-name-block/update-name?user-id=123222").execute()
                assertThat(response.statusCode()).isEqualTo(200)
            }
        }

        @Nested
        inner class IntroductionTextBlock {
            @Test
            fun returnsIntroContent() {
                val response = Jsoup.connect("$BASE_URL/page-block/page-with-blocks-and-page-api-route/blocks/intro-text/introduction-text?user-id=123222").execute()
                val expectedContent = "introduction"
                assertThat(response.body().lowercase()).contains(expectedContent)
            }

            @Test
            fun returns200Status() {
                val response = Jsoup.connect("$BASE_URL/page-block/page-with-blocks-and-page-api-route/blocks/intro-text/introduction-text?user-id=123222").execute()
                assertThat(response.statusCode()).isEqualTo(200)
            }
        }

        @Nested
        inner class WelcomeBlock {
            @Test
            fun returnsWelcomeContent() {
                val response = Jsoup.connect("$BASE_URL/page-block/page-with-blocks-and-page-api-route/blocks/welcome-block/welcome?user-id=123222").execute()
                val expectedContent = "welcome"
                assertThat(response.body().lowercase()).contains(expectedContent)
            }

            @Test
            fun returns200Status() {
                val response = Jsoup.connect("$BASE_URL/page-block/page-with-blocks-and-page-api-route/blocks/welcome-block/welcome?user-id=123222").execute()
                assertThat(response.statusCode()).isEqualTo(200)
            }
        }

        @Nested
        inner class UpdateFirstNamePageBlockApi {
            @Test
            fun returnsSuccessJson() {
                val response =
                    Jsoup
                        .connect("$BASE_URL/page-block-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name?user-id=123222&first-name=John")
                        .ignoreContentType(true)
                        .execute()
                val expectedContent = "success"
                assertThat(response.body().lowercase()).contains(expectedContent)
            }

            @Test
            fun returns200Status() {
                val response =
                    Jsoup
                        .connect("$BASE_URL/page-block-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name?user-id=123222&first-name=John")
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
            val response = Jsoup.connect("$BASE_URL/page-with-custom-status").execute()
            assertThat(response.statusCode()).isEqualTo(201)
        }

        @Test
        fun containsStatusContent() {
            val response = Jsoup.connect("$BASE_URL/page-with-custom-status").execute()
            val expectedContent = "custom status"
            assertThat(response.body().lowercase()).contains(expectedContent)
        }
    }

    @Nested
    inner class PageWithHeaders {
        @Test
        fun containsCustomHeaders() {
            val response = Jsoup.connect("$BASE_URL/page-with-headers").execute()
            assertThat(response.header("X-Page-Type")).isNotNull()
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$BASE_URL/page-with-headers").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }

    @Nested
    inner class JavaStylePage {
        @Test
        fun returnsJavaStyleContent() {
            val response = Jsoup.connect("$BASE_URL/java-style-page").execute()
            val expectedContent = "java"
            assertThat(response.body().lowercase()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$BASE_URL/java-style-page").execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }
}
