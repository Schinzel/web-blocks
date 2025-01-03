package io.schinzel.page_elements.web.response_handlers

import io.schinzel.page_elements.web.response_handlers.ResponseHandlerUtil.removeSuffixes
import io.schinzel.page_elements.web.response_handlers.ResponseHandlerUtil.removeSuffixesAndToKebabCase
import io.schinzel.page_elements.web.response_handlers.ResponseHandlerUtil.toKebabCase
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
        fun `class has one suffix to remove - suffix removed`() {
            val suffixesToRemove = listOf("Api", "Endpoint")
            val className = "MyClassApi"
            val actual = removeSuffixes(className, suffixesToRemove)
            val expected = "MyClass"
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `class has no suffix to remove - class name unchanged`() {
            val suffixesToRemove = listOf("Api", "Endpoint")
            val className = "MyClass"
            val actual = removeSuffixes(className, suffixesToRemove)
            val expected = "MyClass"
            assertThat(actual).isEqualTo(expected)
        }


        @Test
        fun `empty list of suffixes - class name unchanged`() {
            val suffixesToRemove = emptyList<String>()
            val className = "MyClass"
            val actual = removeSuffixes(className, suffixesToRemove)
            val expected = "MyClass"
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `one suffix, case not matching - suffix removed`() {
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
        fun `Three words - in kebab case`() {
            val className = "MyClassName"
            val actual = toKebabCase(className)
            val expected = "my-class-name"
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `One word - in kebab case`() {
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
        fun `Class in pascal case with suffix - in kebab case and suffix removed`() {
            val actual = removeSuffixesAndToKebabCase(MyClassApi::class, listOf("Api", "Endpoint"))
            val expected = "my-class"
            assertThat(actual).isEqualTo(expected)
        }

    }
    private class MyClassApi
}