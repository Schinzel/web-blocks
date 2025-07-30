package io.schinzel.sample

import io.schinzel.web_blocks.test_utils.PortUtil
import io.schinzel.web_blocks.web.WebBlocksApp
import io.schinzel.web_blocks.web.request_handler.log.NoLogger
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorRegistry
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

class MyWebAppApiTest(
    testPort: Int,
) : WebBlocksApp() {
    override val logger = NoLogger()
    override val port: Int = testPort
}

/**
 * The purpose of this class is to test all sample API routes and content
 * to ensure they work correctly after refactoring.
 *
 * Written by Claude Sonnet 4
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SampleApiTest {
    companion object {
        private lateinit var baseUrl: String
        private lateinit var app: MyWebAppApiTest
    }

    @BeforeAll
    fun startApplication() {
        val port = PortUtil.findAvailablePort()
        baseUrl = "http://127.0.0.1:$port"
        app = MyWebAppApiTest(port)
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
    inner class UserPetsApi {
        @Test
        fun returnsPetsList() {
            val response = Jsoup.connect("$baseUrl/api/user-pets").ignoreContentType(true).execute()
            val expectedContent = "Fluffy"
            assertThat(response.body()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$baseUrl/api/user-pets").ignoreContentType(true).execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }

    @Nested
    inner class UserInformationApi {
        @Test
        fun returnsUserData() {
            val response = Jsoup.connect("$baseUrl/api/user-information-endpoint?user-id=123").ignoreContentType(true).execute()
            val expectedContent = "123"
            assertThat(response.body()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$baseUrl/api/user-information-endpoint?user-id=123").ignoreContentType(true).execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }

    @Test
    fun getResponse_apiThatThrowsError_returns500Status() {
        val response =
            Jsoup
                .connect("$baseUrl/api/api-that-throws-error")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute()
        assertThat(response.statusCode()).isEqualTo(500)
    }

    @Nested
    inner class UserApiWithHeaders {
        @Test
        fun containsCustomHeaders() {
            val response = Jsoup.connect("$baseUrl/api/user-api-with-headers?user-id=123").ignoreContentType(true).execute()
            assertThat(response.header("X-Total-Count")).isNotNull()
        }

        @Test
        fun returnsJsonResponse() {
            val response = Jsoup.connect("$baseUrl/api/user-api-with-headers?user-id=123").ignoreContentType(true).execute()
            val expectedContent = "123"
            assertThat(response.body()).contains(expectedContent)
        }
    }
}
