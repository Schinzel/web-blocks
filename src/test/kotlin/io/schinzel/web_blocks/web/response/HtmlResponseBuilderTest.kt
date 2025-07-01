package io.schinzel.web_blocks.web.response

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test the HtmlResponseBuilder for cross-JVM language compatibility.
 *
 * Written by Claude Sonnet 4
 */
class HtmlResponseBuilderTest {

    @Nested
    @DisplayName("setContent")
    inner class SetContent {

        @Test
        fun `setContent _ with valid content _ content is set correctly`() {
            val content = "<h1>Hello World</h1>"
            val response = HtmlResponse.builder()
                .setContent(content)
                .build()
            
            assertThat(response.content).isEqualTo(content)
        }

        @Test
        fun `setContent _ called twice _ second call replaces first`() {
            val response = HtmlResponse.builder()
                .setContent("<h1>First</h1>")
                .setContent("<h1>Second</h1>")
                .build()
                
            assertThat(response.content).isEqualTo("<h1>Second</h1>")
        }

        @Test
        fun `setContent _ returns builder _ allows method chaining`() {
            val builder = HtmlResponse.builder()
            val result = builder.setContent("<p>Test</p>")
            
            assertThat(result).isSameAs(builder)
        }
    }

    @Nested
    @DisplayName("setStatus")
    inner class SetStatus {

        @Test
        fun `setStatus _ with custom status _ status is set correctly`() {
            val expectedStatus = 201
            val response = HtmlResponse.builder()
                .setContent("<p>Created</p>")
                .setStatus(expectedStatus)
                .build()
                
            assertThat(response.status).isEqualTo(expectedStatus)
        }

        @Test
        fun `setStatus _ called twice _ second call replaces first`() {
            val response = HtmlResponse.builder()
                .setContent("<p>Test</p>")
                .setStatus(201)
                .setStatus(404)
                .build()
                
            assertThat(response.status).isEqualTo(404)
        }

        @Test
        fun `setStatus _ not called _ defaults to 200`() {
            val response = HtmlResponse.builder()
                .setContent("<p>Test</p>")
                .build()
                
            assertThat(response.status).isEqualTo(200)
        }
    }

    @Nested
    @DisplayName("addHeader")
    inner class AddHeader {

        @Test
        fun `addHeader _ single header _ header is added correctly`() {
            val response = HtmlResponse.builder()
                .setContent("<p>Test</p>")
                .addHeader("X-Custom-Header", "test-value")
                .build()
                
            val headers = response.headers
            assertThat(headers).hasSize(1)
            assertThat(headers["X-Custom-Header"]).isEqualTo("test-value")
        }

        @Test
        fun `addHeader _ called multiple times _ all headers are included`() {
            val response = HtmlResponse.builder()
                .setContent("<p>Test</p>")
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
            val response = HtmlResponse.builder()
                .setContent("<p>Test</p>")
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
            val response = HtmlResponse.builder()
                .setContent("<p>Test</p>")
                .addHeaders(headersToAdd)
                .build()
                
            val headers = response.headers
            assertThat(headers).hasSize(2)
            assertThat(headers["X-First"]).isEqualTo("1")
            assertThat(headers["X-Second"]).isEqualTo("2")
        }

        @Test
        fun `addHeaders _ merges with existing headers _ all headers present`() {
            val response = HtmlResponse.builder()
                .setContent("<p>Test</p>")
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
            val response = HtmlResponse.builder()
                .setContent("<p>Test</p>")
                .addHeaders(emptyMap())
                .build()
                
            assertThat(response.headers).isEmpty()
        }
    }

    @Nested
    @DisplayName("build")
    inner class Build {

        @Test
        fun `build _ content not set _ throws IllegalArgumentException`() {
            val builder = HtmlResponse.builder()
                .setStatus(200)
                
            assertThatThrownBy { builder.build() }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("HTML content cannot be empty")
        }

        @Test
        fun `build _ empty content _ throws IllegalArgumentException`() {
            val builder = HtmlResponse.builder()
                .setContent("")
                
            assertThatThrownBy { builder.build() }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("HTML content cannot be empty")
        }

        @Test
        fun `build _ with all valid data _ creates HtmlResponse correctly`() {
            val content = "<h1>Test Page</h1>"
            val status = 201
            val headers = mapOf("X-Custom" to "value")
            
            val response = HtmlResponse.builder()
                .setContent(content)
                .setStatus(status)
                .addHeaders(headers)
                .build()
                
            assertThat(response.content).isEqualTo(content)
            assertThat(response.status).isEqualTo(status)
            assertThat(response.headers).isEqualTo(headers)
        }

        @Test
        fun `build _ headers are immutable _ original builder not affected by changes`() {
            val builder = HtmlResponse.builder()
                .setContent("<p>Test</p>")
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
            val response = HtmlResponse.builder()
                .setContent("<h1>Java Style</h1>")
                .setStatus(201)
                .addHeader("X-Language", "Java")
                .build()
                
            assertThat(response.content).isEqualTo("<h1>Java Style</h1>")
            assertThat(response.status).isEqualTo(201)
            assertThat(response.headers["X-Language"]).isEqualTo("Java")
        }

        @Test
        fun `builder _ method chaining order independence _ any order works`() {
            val response1 = HtmlResponse.builder()
                .setContent("<p>Content first</p>")
                .setStatus(200)
                .addHeader("X-Order", "1")
                .build()
                
            val response2 = HtmlResponse.builder()
                .addHeader("X-Order", "2")
                .setStatus(200)
                .setContent("<p>Content last</p>")
                .build()
                
            assertThat(response1.content).isEqualTo("<p>Content first</p>")
            assertThat(response2.content).isEqualTo("<p>Content last</p>")
            assertThat(response1.status).isEqualTo(response2.status)
        }
    }
}