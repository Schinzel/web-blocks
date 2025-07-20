package io.schinzel.web_blocks.web.routes

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test ReturnTypeEnum for route type mapping and content type handling.
 *
 * Written by Claude Sonnet 4
 */
class ReturnTypeEnumTest {
    @Nested
    @DisplayName("getContentType")
    inner class GetContentTypeTests {
        @Test
        fun `getContentType _ HTML return type _ returns text slash html`() {
            val result = ReturnTypeEnum.HTML.getContentType()

            assertThat(result).isEqualTo("text/html")
        }

        @Test
        fun `getContentType _ JSON return type _ returns application slash json`() {
            val result = ReturnTypeEnum.JSON.getContentType()

            assertThat(result).isEqualTo("application/json")
        }
    }
}
