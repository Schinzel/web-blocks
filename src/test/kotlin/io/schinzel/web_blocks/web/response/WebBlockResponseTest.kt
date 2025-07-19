package io.schinzel.web_blocks.web.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test the WebBlockResponse system and its implementations.
 *
 * Written by Claude Sonnet 4
 */
class WebBlockResponseTest {
    @Nested
    @DisplayName("HtmlResponse")
    inner class HtmlResponseTests {
        @Test
        fun `constructor _ with content only _ creates response with default status and headers`() {
            val content = "<h1>Hello World</h1>"
            val response = HtmlContentResponse(content)

            assertThat(response.content).isEqualTo(content)
        }

        @Test
        fun `constructor _ with default values _ status is 200`() {
            val response = HtmlContentResponse("<p>Test</p>")

            assertThat(response.status).isEqualTo(200)
        }

        @Test
        fun `constructor _ with default values _ headers are empty`() {
            val response = HtmlContentResponse("<p>Test</p>")

            assertThat(response.headers).isEmpty()
        }

        @Test
        fun `constructor _ with custom status _ status is set correctly`() {
            val expectedStatus = 201
            val response = HtmlContentResponse("<p>Created</p>", status = expectedStatus)

            assertThat(response.status).isEqualTo(expectedStatus)
        }

        @Test
        fun `constructor _ with custom headers _ headers are set correctly`() {
            val expectedHeaders = mapOf("X-Custom-Header" to "test-value")
            val response = HtmlContentResponse("<p>Test</p>", headers = expectedHeaders)

            assertThat(response.headers).isEqualTo(expectedHeaders)
        }
    }

    @Nested
    @DisplayName("JsonResponse")
    inner class JsonResponseTests {
        @Test
        fun `constructor _ with data object _ creates response with correct data`() {
            val testData = mapOf("message" to "success", "count" to 42)
            val response = JsonSuccessResponse(testData)

            assertThat(response.data).isEqualTo(testData)
        }

        @Test
        fun `constructor _ with default values _ status is 200`() {
            val response = JsonSuccessResponse("test data")

            assertThat(response.status).isEqualTo(200)
        }

        @Test
        fun `constructor _ with default values _ headers are empty`() {
            val response = JsonSuccessResponse("test data")

            assertThat(response.headers).isEmpty()
        }

        @Test
        fun `constructor _ with custom status _ status is set correctly`() {
            val expectedStatus = 404
            val response = JsonSuccessResponse("Not found", status = expectedStatus)

            assertThat(response.status).isEqualTo(expectedStatus)
        }

        @Test
        fun `constructor _ with list data _ preserves list structure`() {
            val testList = listOf("item1", "item2", "item3")
            val response = JsonSuccessResponse(testList)

            assertThat(response.data).isEqualTo(testList)
        }

        @Test
        fun `constructor _ with data class _ preserves object structure`() {
            val testUser = TestUser("John", 30)
            val response = JsonSuccessResponse(testUser)

            assertThat(response.data).isEqualTo(testUser)
        }
    }

    @Nested
    @DisplayName("WebBlockResponse interface")
    inner class WebBlockResponseInterfaceTests {
        @Test
        fun `HtmlResponse _ implements WebBlockResponse _ is instance of interface`() {
            val response: IWebBlockResponse = HtmlContentResponse("<p>Test</p>")

            assertThat(response).isInstanceOf(IWebBlockResponse::class.java)
        }

        @Test
        fun `JsonResponse _ implements WebBlockResponse _ is instance of interface`() {
            val response: IWebBlockResponse = JsonSuccessResponse("test")

            assertThat(response).isInstanceOf(IWebBlockResponse::class.java)
        }

        @Test
        fun `responses _ when cast to interface _ retain access to properties`() {
            val htmlResponse: IWebBlockResponse = HtmlContentResponse("<p>Test</p>", status = 201)
            val jsonSuccessResponse: IWebBlockResponse = JsonSuccessResponse("data", status = 404)

            assertThat(htmlResponse.status).isEqualTo(201)
            assertThat(jsonSuccessResponse.status).isEqualTo(404)
        }
    }

    /**
     * The purpose of this data class is to test JSON response handling with custom objects.
     */
    private data class TestUser(
        val name: String,
        val age: Int,
    )
}
