package io.schinzel.sample

import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * The purpose of this class is to test all sample API routes and content
 * to ensure they work correctly after refactoring.
 *
 * Written by Claude Sonnet 4
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SampleApiTest {
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
    inner class UserPetsApi {
        @Test
        fun returnsPetsList() {
            val response = Jsoup.connect("$BASE_URL/api/user-pets").ignoreContentType(true).execute()
            val expectedContent = "Fluffy"
            assertThat(response.body()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$BASE_URL/api/user-pets").ignoreContentType(true).execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }

    @Nested
    inner class UserInformationApi {
        @Test
        fun returnsUserData() {
            val response = Jsoup.connect("$BASE_URL/api/user-information-endpoint?user-id=123").ignoreContentType(true).execute()
            val expectedContent = "123"
            assertThat(response.body()).contains(expectedContent)
        }

        @Test
        fun returns200Status() {
            val response = Jsoup.connect("$BASE_URL/api/user-information-endpoint?user-id=123").ignoreContentType(true).execute()
            assertThat(response.statusCode()).isEqualTo(200)
        }
    }

    @Test
    fun getResponse_apiRouteThatThrowsError_returns200Status() {
        val response =
            Jsoup
                .connect("$BASE_URL/api/api-route-that-throws-error")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute()
        assertThat(response.statusCode()).isEqualTo(200)
    }

    @Nested
    inner class UserApiWithHeaders {
        @Test
        fun containsCustomHeaders() {
            val response = Jsoup.connect("$BASE_URL/api/user-api-with-headers?user-id=123").ignoreContentType(true).execute()
            assertThat(response.header("X-Total-Count")).isNotNull()
        }

        @Test
        fun returnsJsonResponse() {
            val response = Jsoup.connect("$BASE_URL/api/user-api-with-headers?user-id=123").ignoreContentType(true).execute()
            val expectedContent = "123"
            assertThat(response.body()).contains(expectedContent)
        }
    }
}
