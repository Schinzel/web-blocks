package io.schinzel.web_blocks.component.template_engine

import io.schinzel.web_blocks.component.template_engine.file_reader.IFileReader
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test the TemplateProcessor functionality
 * including basic variable substitution, loops, and object property access.
 *
 * Written by Claude Sonnet 4
 */
class SimpleTemplateProcessorTest {
    companion object {
        private const val EMPTY_PATH = ""
        private const val TEST_FILE_NAME = "test.html"
        private const val EXPECTED_HELLO_WORLD = "<h1>Hello World!</h1>"
        private const val EXPECTED_COLOR_SPANS = "<span>red</span><span>green</span><span>blue</span>"
        private const val EXPECTED_EMPTY = ""
        private const val EXPECTED_USER_LIST = "<li>Anna - anna@example.com</li><li>Bob - bob@example.com</li>"
        private const val EXPECTED_ERROR_MESSAGE = "Invalid loop syntax"
    }

    @Nested
    inner class BasicVariableSubstitution {
        @Test
        fun withData_stringValue_replacesPlaceholder() {
            val mockFileReader =
                object : IFileReader {
                    override val pathToCaller: String = EMPTY_PATH

                    override fun getFileContent(fileName: String): String = "<h1>Hello {{name}}!</h1>"
                }

            val processor = TemplateProcessor(mockFileReader)
            val result = processor.withData("name", "World").processTemplate(TEST_FILE_NAME)

            assertThat(result).isEqualTo(EXPECTED_HELLO_WORLD)
        }
    }

    @Nested
    inner class SimpleListLoops {
        @Test
        fun processTemplate_simpleStringList_expandsLoop() {
            val mockFileReader =
                object : IFileReader {
                    override val pathToCaller: String = EMPTY_PATH

                    override fun getFileContent(fileName: String): String = "{{for color in colors}}<span>{{color}}</span>{{/for}}"
                }

            val processor = TemplateProcessor(mockFileReader)
            val result = processor.withData("colors", listOf("red", "green", "blue")).processTemplate(TEST_FILE_NAME)

            assertThat(result).isEqualTo(EXPECTED_COLOR_SPANS)
        }

        @Test
        fun processTemplate_emptyList_producesEmptyContent() {
            val mockFileReader =
                object : IFileReader {
                    override val pathToCaller: String = EMPTY_PATH

                    override fun getFileContent(fileName: String): String = "{{for item in items}}<p>{{item}}</p>{{/for}}"
                }

            val processor = TemplateProcessor(mockFileReader)
            val result = processor.withData("items", emptyList<String>()).processTemplate(TEST_FILE_NAME)

            assertThat(result).isEqualTo(EXPECTED_EMPTY)
        }
    }

    @Nested
    inner class ObjectListLoops {
        @Test
        fun processTemplate_objectList_expandsLoopWithProperties() {
            data class User(
                val name: String,
                val email: String,
            )

            val mockFileReader =
                object : IFileReader {
                    override val pathToCaller: String = EMPTY_PATH

                    override fun getFileContent(fileName: String): String = "{{for user in users}}<li>{{user.name}} - {{user.email}}</li>{{/for}}"
                }

            val processor = TemplateProcessor(mockFileReader)
            val users = listOf(User("Anna", "anna@example.com"), User("Bob", "bob@example.com"))
            val result = processor.withData("users", users).processTemplate(TEST_FILE_NAME)

            assertThat(result).isEqualTo(EXPECTED_USER_LIST)
        }
    }

    @Nested
    inner class EdgeCases {
        @Test
        fun processTemplate_invalidLoopSyntax_throwsException() {
            val mockFileReader =
                object : IFileReader {
                    override val pathToCaller: String = EMPTY_PATH

                    override fun getFileContent(fileName: String): String = "{{for invalid syntax}}<p>test</p>{{/for}}"
                }

            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy {
                processor.processTemplate(TEST_FILE_NAME)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining(EXPECTED_ERROR_MESSAGE)
        }

        @Test
        fun processTemplate_missingListData_usesEmptyList() {
            val mockFileReader =
                object : IFileReader {
                    override val pathToCaller: String = EMPTY_PATH

                    override fun getFileContent(fileName: String): String = "{{for item in nonExistentList}}<p>{{item}}</p>{{/for}}"
                }

            val processor = TemplateProcessor(mockFileReader)
            val result = processor.processTemplate(TEST_FILE_NAME)

            assertThat(result).isEqualTo(EXPECTED_EMPTY)
        }
    }
}
