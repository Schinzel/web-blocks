package io.schinzel.web_blocks.web.response

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test the JsonResponseBuilder for cross-JVM language compatibility.
 *
 * Written by Claude Sonnet 4
 */
class JsonResponseBuilderTest {
    @Nested
    @DisplayName("setData")
    inner class SetData {
        @Test
        fun `setData _ with string data _ data is set correctly`() {
            val data = "test string"
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData(data)
                    .build()

            assertThat(response.data).isEqualTo(data)
        }

        @Test
        fun `setData _ with map data _ data is set correctly`() {
            val data = mapOf("key" to "value", "count" to 42)
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData(data)
                    .build()

            assertThat(response.data).isEqualTo(data)
        }

        @Test
        fun `setData _ with list data _ data is set correctly`() {
            val data = listOf("item1", "item2", "item3")
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData(data)
                    .build()

            assertThat(response.data).isEqualTo(data)
        }

        @Test
        fun `setData _ called twice _ second call replaces first`() {
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData("first")
                    .setData("second")
                    .build()

            assertThat(response.data).isEqualTo("second")
        }

        @Test
        fun `setData _ returns builder _ allows method chaining`() {
            val builder = JsonSuccessResponse.builder()
            val result = builder.setData("test")

            assertThat(result).isSameAs(builder)
        }

        @Test
        fun `setData _ with custom object _ preserves object structure`() {
            val user = TestUser("John", 30)
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData(user)
                    .build()

            assertThat(response.data).isEqualTo(user)
        }
    }

    @Nested
    @DisplayName("setStatus")
    inner class SetStatus {
        @Test
        fun `setStatus _ with custom status _ status is set correctly`() {
            val expectedStatus = 201
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData("created")
                    .setStatus(expectedStatus)
                    .build()

            assertThat(response.status).isEqualTo(expectedStatus)
        }

        @Test
        fun `setStatus _ called twice _ second call replaces first`() {
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData("test")
                    .setStatus(201)
                    .setStatus(404)
                    .build()

            assertThat(response.status).isEqualTo(404)
        }

        @Test
        fun `setStatus _ not called _ defaults to 200`() {
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData("test")
                    .build()

            assertThat(response.status).isEqualTo(200)
        }
    }

    @Nested
    @DisplayName("addHeader")
    inner class AddHeader {
        @Test
        fun `addHeader _ single header _ header is added correctly`() {
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData("test")
                    .addHeader("X-Custom-Header", "test-value")
                    .build()

            val headers = response.headers
            assertThat(headers).hasSize(1)
            assertThat(headers["X-Custom-Header"]).isEqualTo("test-value")
        }

        @Test
        fun `addHeader _ called multiple times _ all headers are included`() {
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData("test")
                    .addHeader("X-First", "1")
                    .addHeader("X-Second", "2")
                    .build()

            val headers = response.headers
            assertThat(headers).hasSize(2)
            assertThat(headers["X-First"]).isEqualTo("1")
            assertThat(headers["X-Second"]).isEqualTo("2")
        }

        @Test
        fun `addHeader _ same key twice _ second value replaces first`() {
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData("test")
                    .addHeader("X-Value", "first")
                    .addHeader("X-Value", "second")
                    .build()

            val headers = response.headers
            assertThat(headers).hasSize(1)
            assertThat(headers["X-Value"]).isEqualTo("second")
        }
    }

    @Nested
    @DisplayName("addHeaders")
    inner class AddHeaders {
        @Test
        fun `addHeaders _ with map _ all headers are added`() {
            val headersToAdd = mapOf("X-First" to "1", "X-Second" to "2")
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData("test")
                    .addHeaders(headersToAdd)
                    .build()

            val headers = response.headers
            assertThat(headers).hasSize(2)
            assertThat(headers["X-First"]).isEqualTo("1")
            assertThat(headers["X-Second"]).isEqualTo("2")
        }

        @Test
        fun `addHeaders _ merges with existing headers _ all headers present`() {
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData("test")
                    .addHeader("X-Existing", "existing")
                    .addHeaders(mapOf("X-New" to "new"))
                    .build()

            val headers = response.headers
            assertThat(headers).hasSize(2)
            assertThat(headers["X-Existing"]).isEqualTo("existing")
            assertThat(headers["X-New"]).isEqualTo("new")
        }

        @Test
        fun `addHeaders _ with empty map _ no headers added`() {
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData("test")
                    .addHeaders(emptyMap())
                    .build()

            assertThat(response.headers).isEmpty()
        }
    }

    @Nested
    @DisplayName("build")
    inner class Build {
        @Test
        fun `build _ data not set _ throws IllegalArgumentException`() {
            val builder =
                JsonSuccessResponse
                    .builder()
                    .setStatus(200)

            assertThatThrownBy { builder.build() }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("JSON data cannot be null")
        }

        @Test
        fun `build _ with all valid data _ creates JsonResponse correctly`() {
            val data = mapOf("message" to "success")
            val status = 201
            val headers = mapOf("X-Custom" to "value")

            val response =
                JsonSuccessResponse
                    .builder()
                    .setData(data)
                    .setStatus(status)
                    .addHeaders(headers)
                    .build()

            assertThat(response.data).isEqualTo(data)
            assertThat(response.status).isEqualTo(status)
            assertThat(response.headers).isEqualTo(headers)
        }

        @Test
        fun `build _ headers are immutable _ original builder not affected by changes`() {
            val builder =
                JsonSuccessResponse
                    .builder()
                    .setData("test")
                    .addHeader("X-Original", "value")

            val response = builder.build()
            builder.addHeader("X-After-Build", "should-not-affect")

            assertThat(response.headers).hasSize(1)
            assertThat(response.headers["X-Original"]).isEqualTo("value")
            assertThat(response.headers).doesNotContainKey("X-After-Build")
        }
    }

    @Nested
    @DisplayName("Cross-language compatibility simulation")
    inner class CrossLanguageCompatibility {
        @Test
        fun `builder _ java style method calls _ works without named parameters`() {
            val data = mapOf("user" to "john", "role" to "admin")
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData(data)
                    .setStatus(201)
                    .addHeader("X-Language", "Java")
                    .build()

            assertThat(response.data).isEqualTo(data)
            assertThat(response.status).isEqualTo(201)
            assertThat(response.headers["X-Language"]).isEqualTo("Java")
        }

        @Test
        fun `builder _ method chaining order independence _ any order works`() {
            val response1 =
                JsonSuccessResponse
                    .builder()
                    .setData("data first")
                    .setStatus(200)
                    .addHeader("X-Order", "1")
                    .build()

            val response2 =
                JsonSuccessResponse
                    .builder()
                    .addHeader("X-Order", "2")
                    .setStatus(200)
                    .setData("data last")
                    .build()

            assertThat(response1.data).isEqualTo("data first")
            assertThat(response2.data).isEqualTo("data last")
            assertThat(response1.status).isEqualTo(response2.status)
        }

        @Test
        fun `builder _ complex nested data _ preserves structure`() {
            val complexData =
                mapOf(
                    "user" to TestUser("Alice", 25),
                    "preferences" to mapOf("theme" to "dark", "notifications" to true),
                    "items" to listOf("item1", "item2"),
                )

            val response =
                JsonSuccessResponse
                    .builder()
                    .setData(complexData)
                    .build()

            assertThat(response.data).isEqualTo(complexData)
        }
    }

    @Nested
    @DisplayName("Static factory method")
    inner class StaticFactoryMethod {
        @Test
        fun `JsonResponse builder _ creates new instance _ each call independent`() {
            val builder1 = JsonSuccessResponse.builder()
            val builder2 = JsonSuccessResponse.builder()

            assertThat(builder1).isNotSameAs(builder2)
        }

        @Test
        fun `JsonResponse builder _ from companion object _ works correctly`() {
            val response =
                JsonSuccessResponse
                    .builder()
                    .setData("test")
                    .build()

            assertThat(response).isInstanceOf(JsonSuccessResponse::class.java)
            assertThat(response.data).isEqualTo("test")
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
