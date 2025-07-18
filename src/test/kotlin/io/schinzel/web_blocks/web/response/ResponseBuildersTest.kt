package io.schinzel.web_blocks.web.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test the convenience functions for creating WebBlockResponse objects.
 *
 * Written by Claude Sonnet 4
 */
class ResponseBuildersTest {
    @Nested
    @DisplayName("html function")
    inner class HtmlFunction {
        @Test
        fun `html _ with content only _ creates HtmlResponse with default values`() {
            val content = "<h1>Hello World</h1>"
            val response = html(content)

            assertThat(response).isInstanceOf(HtmlResponse::class.java)
        }

        @Test
        fun `html _ with content only _ content is set correctly`() {
            val content = "<p>Test content</p>"
            val response = html(content)

            assertThat(response.content).isEqualTo(content)
        }

        @Test
        fun `html _ with content only _ default status is 200`() {
            val response = html("<p>Test</p>")

            assertThat(response.status).isEqualTo(200)
        }

        @Test
        fun `html _ with custom status _ status is set correctly`() {
            val expectedStatus = 201
            val response = html("<p>Created</p>", status = expectedStatus)

            assertThat(response.status).isEqualTo(expectedStatus)
        }

        @Test
        fun `html _ with custom headers _ headers are set correctly`() {
            val expectedHeaders = mapOf("Cache-Control" to "no-cache")
            val response = html("<p>Test</p>", headers = expectedHeaders)

            assertThat(response.headers).isEqualTo(expectedHeaders)
        }

        @Test
        fun `html _ with all parameters _ all values are set correctly`() {
            val content = "<h1>Custom Response</h1>"
            val status = 418
            val headers = mapOf("X-Tea-Pot" to "true")
            val response = html(content, status, headers)

            assertThat(response.content).isEqualTo(content)
            assertThat(response.status).isEqualTo(status)
            assertThat(response.headers).isEqualTo(headers)
        }
    }

    @Nested
    @DisplayName("json function")
    inner class JsonFunction {
        @Test
        fun `json _ with data only _ creates JsonResponse with default values`() {
            val data = mapOf("key" to "value")
            val response = json(data)

            assertThat(response).isInstanceOf(JsonResponse::class.java)
        }

        @Test
        fun `json _ with data only _ data is set correctly`() {
            val data = listOf("item1", "item2")
            val response = json(data)

            assertThat(response.data).isEqualTo(data)
        }

        @Test
        fun `json _ with data only _ default status is 200`() {
            val response = json("test")

            assertThat(response.status).isEqualTo(200)
        }

        @Test
        fun `json _ with custom status _ status is set correctly`() {
            val expectedStatus = 404
            val response = json("Not found", status = expectedStatus)

            assertThat(response.status).isEqualTo(expectedStatus)
        }

        @Test
        fun `json _ with custom headers _ headers are set correctly`() {
            val expectedHeaders = mapOf("Content-Type" to "application/json")
            val response = json("test", headers = expectedHeaders)

            assertThat(response.headers).isEqualTo(expectedHeaders)
        }

        @Test
        fun `json _ with string data _ preserves string value`() {
            val data = "Simple string response"
            val response = json(data)

            assertThat(response.data).isEqualTo(data)
        }

        @Test
        fun `json _ with complex object _ preserves object structure`() {
            val complexData = TestResponse("success", 123, listOf("a", "b"))
            val response = json(complexData)

            assertThat(response.data).isEqualTo(complexData)
        }
    }

    @Nested
    @DisplayName("function return types")
    inner class FunctionReturnTypes {
        @Test
        fun `html function _ returns WebBlockResponse type _ can be assigned to interface`() {
            val response: WebBlockResponse = html("<p>Test</p>")

            assertThat(response).isNotNull()
        }

        @Test
        fun `json function _ returns WebBlockResponse type _ can be assigned to interface`() {
            val response: WebBlockResponse = json("test data")

            assertThat(response).isNotNull()
        }

        @Test
        fun `both functions _ return different implementations _ are distinguishable`() {
            val htmlResponse = html("<p>HTML</p>")
            val jsonResponse = json("JSON")

            assertThat(htmlResponse).isInstanceOf(HtmlResponse::class.java)
            assertThat(jsonResponse).isInstanceOf(JsonResponse::class.java)
        }
    }

    /**
     * The purpose of this data class is to test JSON response handling with complex objects.
     */
    private data class TestResponse(
        val status: String,
        val count: Int,
        val items: List<String>,
    )
}
