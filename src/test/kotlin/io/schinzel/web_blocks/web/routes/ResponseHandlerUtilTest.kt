package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.route_descriptors.RouteUtil.removeSuffixes
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteUtil.removeSuffixesAndToKebabCase
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteUtil.toKebabCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ResponseHandlerUtilTest {
    /**
     * Case insensitive
     * Remove several suffixes??
     */
    @Nested
    @DisplayName("removeSuffixes")
    inner class RemoveSuffixes {
        @Test
        fun `class with one suffix _ suffix removed`() {
            val suffixesToRemove = listOf("Api", "Endpoint")
            val className = "MyClassApi"
            val actual = removeSuffixes(className, suffixesToRemove)
            val expected = "MyClass"
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `class with no suffix _ class name unchanged`() {
            val suffixesToRemove = listOf("Api", "Endpoint")
            val className = "MyClass"
            val actual = removeSuffixes(className, suffixesToRemove)
            val expected = "MyClass"
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `empty list of suffixes _ class name unchanged`() {
            val suffixesToRemove = emptyList<String>()
            val className = "MyClass"
            val actual = removeSuffixes(className, suffixesToRemove)
            val expected = "MyClass"
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `suffix case not matching _ suffix removed`() {
            val suffixesToRemove = listOf("API")
            val className = "MyClassApi"
            val actual = removeSuffixes(className, suffixesToRemove)
            val expected = "MyClass"
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Nested
    @DisplayName("toKebabCase")
    inner class ToKebabCase {
        @Test
        fun `three words _ converted to kebab case`() {
            val className = "MyClassName"
            val actual = toKebabCase(className)
            val expected = "my-class-name"
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `one word _ converted to kebab case`() {
            val className = "Giraffe"
            val actual = toKebabCase(className)
            val expected = "giraffe"
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Nested
    @DisplayName("removeSuffixesAndToKebabCase")
    inner class RemoveSuffixesAndToKebabCase {
        @Test
        fun `class in pascal case with suffix _ converted to kebab case and suffix removed`() {
            val actual = removeSuffixesAndToKebabCase(MyClassApi::class, listOf("Api", "Endpoint"))
            val expected = "my-class"
            assertThat(actual).isEqualTo(expected)
        }
    }

    private class MyClassApi
}
