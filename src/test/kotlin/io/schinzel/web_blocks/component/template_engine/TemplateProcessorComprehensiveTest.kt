package io.schinzel.web_blocks.component.template_engine

import io.schinzel.web_blocks.component.template_engine.file_reader.IFileReader
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException

/**
 * Comprehensive tests for TemplateProcessor, focusing on security, edge cases,
 * and complex production scenarios not covered in SimpleTemplateProcessorTest.
 *
 * Written by Claude Sonnet 4
 */
class TemplateProcessorComprehensiveTest {
    private lateinit var mockFileReader: MockFileReader

    @BeforeEach
    fun setUp() {
        mockFileReader = MockFileReader()
    }

    companion object {
        private const val EMPTY_PATH = ""
        private const val MAX_DEPTH_ERROR = "Max include depth (10) exceeded. Possible circular dependency."
        private const val PATH_TRAVERSAL_ERROR = "Path traversal not allowed in include filenames"
        private const val BLANK_FILENAME_ERROR = "Include filename cannot be blank"
        private const val PROGRESS_ERROR = "Loop processing failed to make progress. Template may be malformed."
        private const val MAX_ITERATIONS_ERROR = "Loop processing exceeded maximum iterations. Template may contain infinite loop."
        private const val INVALID_LOOP_ERROR = "Invalid loop syntax"
    }

    /**
     * A configurable fake file reader for isolated and deterministic testing.
     */
    private class MockFileReader(
        private val files: MutableMap<String, String> = mutableMapOf(),
    ) : IFileReader {
        override val pathToCaller: String = EMPTY_PATH

        override fun getFileContent(fileName: String): String = files[fileName] ?: throw FileNotFoundException("Mock file not found: $fileName")

        fun addFile(
            name: String,
            content: String,
        ) {
            files[name] = content
        }
    }

    @Nested
    inner class SecurityAndValidation {
        @Test
        fun processTemplate_pathTraversalInTemplateFileName_throwsIllegalArgumentException() {
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.processTemplate("../secrets.txt") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Path traversal not allowed in template names")
        }

        @Test
        fun processTemplate_blankTemplateFileName_throwsIllegalArgumentException() {
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.processTemplate("") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Template file name cannot be blank")
        }

        @Test
        fun processTemplate_pathTraversalInIncludeFileName_throwsIllegalArgumentException() {
            val templateContent = "{{include:../secrets.txt}}"
            mockFileReader.addFile("main.html", templateContent)
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.processTemplate("main.html") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(PATH_TRAVERSAL_ERROR)
        }

        @Test
        fun processTemplate_blankIncludeFileName_throwsIllegalArgumentException() {
            val templateContent = "{{include: }}"
            mockFileReader.addFile("main.html", templateContent)
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.processTemplate("main.html") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(BLANK_FILENAME_ERROR)
        }

        @Test
        fun processTemplate_emptyIncludeFileName_throwsIllegalArgumentException() {
            val templateContent = "{{include:}}"
            mockFileReader.addFile("main.html", templateContent)
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.processTemplate("main.html") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(BLANK_FILENAME_ERROR)
        }

        @Test
        fun processTemplate_fileReaderThrowsException_exceptionPropagates() {
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.processTemplate("missing.html") }
                .isInstanceOf(FileNotFoundException::class.java)
                .hasMessage("Mock file not found: missing.html")
        }

        @Test
        fun withData_unsupportedDataType_throwsIllegalArgumentException() {
            val processor = TemplateProcessor(mockFileReader)

            class UnsupportedType

            assertThatThrownBy { processor.withData("key", UnsupportedType()) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Unsupported data type")
        }

        @Test
        fun withData_dateObject_throwsIllegalArgumentException() {
            val processor = TemplateProcessor(mockFileReader)
            val date = java.util.Date()

            assertThatThrownBy { processor.withData("date", date) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Unsupported data type")
        }

        @Test
        fun processTemplate_objectWithPrivateProperty_privatePropertyIsNotRendered() {
            class TestUser(
                val publicName: String,
                private val privateId: String,
            )
            val template = "{{for user in users}}Name: {{user.publicName}}, ID: {{user.privateId}}{{/for}}"
            mockFileReader.addFile("user_list.html", template)
            val processor =
                TemplateProcessor(mockFileReader)
                    .withData("users", listOf(TestUser("John", "secret123")))

            val result = processor.processTemplate("user_list.html")

            assertThat(result).isEqualTo("Name: John, ID: {{user.privateId}}")
        }
    }

    @Nested
    inner class IncludeProcessing {
        @Test
        fun processTemplate_simpleInclude_processesCorrectly() {
            mockFileReader.addFile("main.html", "Header: {{include:header.html}} Footer")
            mockFileReader.addFile("header.html", "<h1>Welcome</h1>")
            val processor = TemplateProcessor(mockFileReader)

            val result = processor.processTemplate("main.html")

            assertThat(result).isEqualTo("Header: <h1>Welcome</h1> Footer")
        }

        @Test
        fun processTemplate_multipleIncludes_processesCorrectly() {
            mockFileReader.addFile("main.html", "{{include:header.html}} Content {{include:footer.html}}")
            mockFileReader.addFile("header.html", "<header>Top</header>")
            mockFileReader.addFile("footer.html", "<footer>Bottom</footer>")
            val processor = TemplateProcessor(mockFileReader)

            val result = processor.processTemplate("main.html")

            assertThat(result).isEqualTo("<header>Top</header> Content <footer>Bottom</footer>")
        }

        @Test
        fun processTemplate_nestedIncludes_areProcessedCorrectly() {
            mockFileReader.addFile("level1.html", "Level 1 start. {{include:level2.html}} Level 1 end.")
            mockFileReader.addFile("level2.html", "Level 2 start. {{include:level3.html}} Level 2 end.")
            mockFileReader.addFile("level3.html", "Level 3 content.")
            val processor = TemplateProcessor(mockFileReader)

            val result = processor.processTemplate("level1.html")

            val expected = "Level 1 start. Level 2 start. Level 3 content. Level 2 end. Level 1 end."
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun processTemplate_includeWithVariables_processesCorrectly() {
            mockFileReader.addFile("main.html", "{{include:greeting.html}}")
            mockFileReader.addFile("greeting.html", "Hello {{name}}!")
            val processor = TemplateProcessor(mockFileReader).withData("name", "World")

            val result = processor.processTemplate("main.html")

            assertThat(result).isEqualTo("Hello World!")
        }

        @Test
        fun processTemplate_includeWithLoops_processesCorrectly() {
            mockFileReader.addFile("main.html", "{{include:list.html}}")
            mockFileReader.addFile("list.html", "{{for item in items}}<li>{{item}}</li>{{/for}}")
            val processor = TemplateProcessor(mockFileReader).withData("items", listOf("A", "B", "C"))

            val result = processor.processTemplate("main.html")

            assertThat(result).isEqualTo("<li>A</li><li>B</li><li>C</li>")
        }

        @Test
        fun processTemplate_maxIncludeDepthExceeded_throwsIllegalStateException() {
            // Create a chain of 11 includes (depth 0 to 10)
            for (i in 0..10) {
                mockFileReader.addFile("level$i.html", "Content {{include:level${i + 1}.html}}")
            }
            mockFileReader.addFile("level11.html", "Final content")
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.processTemplate("level0.html") }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage(MAX_DEPTH_ERROR)
        }

        @Test
        fun processTemplate_circularInclude_throwsIllegalStateException() {
            mockFileReader.addFile("pageA.html", "Page A includes B: {{include:pageB.html}}")
            mockFileReader.addFile("pageB.html", "Page B includes A: {{include:pageA.html}}")
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.processTemplate("pageA.html") }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining("Max include depth")
        }

        @Test
        fun processTemplate_missingIncludeFile_throwsFileNotFoundException() {
            mockFileReader.addFile("main.html", "{{include:missing.html}}")
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.processTemplate("main.html") }
                .isInstanceOf(FileNotFoundException::class.java)
                .hasMessage("Mock file not found: missing.html")
        }

        @Test
        fun processTemplate_malformedIncludeSyntax_ignoresInclude() {
            mockFileReader.addFile("main.html", "Before {{include} After")
            val processor = TemplateProcessor(mockFileReader)

            val result = processor.processTemplate("main.html")

            assertThat(result).isEqualTo("Before {{include} After")
        }

        @Test
        fun processTemplate_unclosedIncludeSyntax_ignoresInclude() {
            mockFileReader.addFile("main.html", "Before {{include:file.html After")
            val processor = TemplateProcessor(mockFileReader)

            val result = processor.processTemplate("main.html")

            assertThat(result).isEqualTo("Before {{include:file.html After")
        }
    }

    @Nested
    inner class AdvancedLooping {
        @Test
        fun processTemplate_nestedLoops_areProcessedCorrectly() {
            val template =
                "{{for team in teams}}" +
                    "<h2>{{team.name}}</h2>" +
                    "<ul>{{for user in team.users}}" +
                    "<li>{{user.name}}</li>" +
                    "{{/for}}</ul>" +
                    "{{/for}}"
            mockFileReader.addFile("teams.html", template)

            data class User(
                val name: String,
            )

            data class Team(
                val name: String,
                val users: List<User>,
            )

            val teams =
                listOf(
                    Team("Admins", listOf(User("Alice"), User("Bob"))),
                    Team("Guests", listOf(User("Charlie"))),
                )
            val processor = TemplateProcessor(mockFileReader).withData("teams", teams)

            val result = processor.processTemplate("teams.html")

            val expected =
                "<h2>Admins</h2><ul><li>Alice</li><li>Bob</li></ul>" +
                    "<h2>Guests</h2><ul><li>Charlie</li></ul>"
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun processTemplate_loopWithComplexObjects_processesCorrectly() {
            data class Product(
                val name: String,
                val price: Int,
                val available: Boolean,
            )
            val template =
                "{{for product in products}}" +
                    "{{product.name}}: \${{product.price}} ({{product.available}})" +
                    "{{/for}}"
            mockFileReader.addFile("products.html", template)

            val products =
                listOf(
                    Product("Laptop", 999, true),
                    Product("Phone", 599, false),
                )
            val processor = TemplateProcessor(mockFileReader).withData("products", products)

            val result = processor.processTemplate("products.html")

            assertThat(result).isEqualTo("Laptop: \$999 (true)Phone: \$599 (false)")
        }

        @Test
        fun processTemplate_loopWithNullPropertyValue_rendersEmptyString() {
            data class Item(
                val name: String,
                val description: String?,
            )
            val template = "{{for item in items}}{{item.name}}: {{item.description}}|{{/for}}"
            mockFileReader.addFile("items.html", template)

            val items = listOf(Item("A", "Description A"), Item("B", null))
            val processor = TemplateProcessor(mockFileReader).withData("items", items)

            val result = processor.processTemplate("items.html")

            assertThat(result).isEqualTo("A: Description A|B: |")
        }

        @Test
        fun processTemplate_loopVariableShadowsGlobalVariable_loopVariableTakesPrecedence() {
            val template = "Global: {{name}}. Loop: {{for name in names}}{{name}},{{/for}}"
            mockFileReader.addFile("shadow.html", template)
            val processor =
                TemplateProcessor(mockFileReader)
                    .withData("name", "GlobalName")
                    .withData("names", listOf("LoopName1", "LoopName2"))

            val result = processor.processTemplate("shadow.html")

            val expected = "Global: GlobalName. Loop: LoopName1,LoopName2,"
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun processTemplate_loopWithMixedDataTypes_processesCorrectly() {
            val template = "{{for item in items}}[{{item}}]{{/for}}"
            mockFileReader.addFile("mixed.html", template)
            val mixedItems = listOf("string", 42, true, 3.14)
            val processor = TemplateProcessor(mockFileReader).withData("items", mixedItems)

            val result = processor.processTemplate("mixed.html")

            assertThat(result).isEqualTo("[string][42][true][3.14]")
        }

        @Test
        fun processTemplate_malformedLoopSyntax_throwsIllegalArgumentException() {
            val testCases =
                listOf(
                    "{{for}}" to "{{for}}",
                    "{{for item}}" to "item",
                    "{{for item in}}" to "item in",
                    "{{for item not in items}}" to "item not in items",
                )

            testCases.forEach { (template, invalidSyntax) ->
                mockFileReader.addFile("malformed_$invalidSyntax.html", "$template content {{/for}}")
                val processor = TemplateProcessor(mockFileReader)

                assertThatThrownBy { processor.processTemplate("malformed_$invalidSyntax.html") }
                    .isInstanceOf(IllegalArgumentException::class.java)
                    .hasMessageContaining(INVALID_LOOP_ERROR)
            }
        }

        @Test
        fun processTemplate_unclosedLoop_noInfiniteLoop() {
            val template = "{{for item in items}} content without end tag"
            mockFileReader.addFile("unclosed.html", template)
            val processor = TemplateProcessor(mockFileReader).withData("items", listOf("A"))

            val result = processor.processTemplate("unclosed.html")

            // Should return template as-is since no valid loop structure found
            assertThat(result).isEqualTo(template)
        }

        @Test
        fun processTemplate_maxIterationsExceeded_throwsIllegalStateException() {
            // Create a template that will trigger max iterations
            val templateBuilder = StringBuilder()
            for (i in 1..101) {
                templateBuilder.append("{{for item$i in items}}content{{/for}}")
            }
            val template = templateBuilder.toString()
            mockFileReader.addFile("many_loops.html", template)
            val processor = TemplateProcessor(mockFileReader).withData("items", listOf("A"))

            assertThatThrownBy { processor.processTemplate("many_loops.html") }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage(MAX_ITERATIONS_ERROR)
        }
    }

    @Nested
    inner class DataTypeHandling {
        @Test
        fun withData_integerValue_convertsToString() {
            mockFileReader.addFile("test.html", "Value: {{number}}")
            val processor = TemplateProcessor(mockFileReader).withData("number", 42)

            val result = processor.processTemplate("test.html")

            assertThat(result).isEqualTo("Value: 42")
        }

        @Test
        fun withData_booleanValue_throwsIllegalArgumentException() {
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.withData("flag", true) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Unsupported data type")
        }

        @Test
        fun withData_longValue_throwsIllegalArgumentException() {
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.withData("bigNumber", 123L) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Unsupported data type")
        }

        @Test
        fun withData_doubleValue_throwsIllegalArgumentException() {
            val processor = TemplateProcessor(mockFileReader)

            assertThatThrownBy { processor.withData("decimal", 3.14) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Unsupported data type")
        }

        @Test
        fun withData_emptyList_processesCorrectly() {
            mockFileReader.addFile("empty.html", "{{for item in items}}Should not appear{{/for}}")
            val processor = TemplateProcessor(mockFileReader).withData("items", emptyList<String>())

            val result = processor.processTemplate("empty.html")

            assertThat(result).isEqualTo("")
        }

        @Test
        fun withData_singleItemList_processesCorrectly() {
            mockFileReader.addFile("single.html", "{{for item in items}}[{{item}}]{{/for}}")
            val processor = TemplateProcessor(mockFileReader).withData("items", listOf("OnlyItem"))

            val result = processor.processTemplate("single.html")

            assertThat(result).isEqualTo("[OnlyItem]")
        }
    }

    @Nested
    inner class TemplateParsingEdgeCases {
        @Test
        fun processTemplate_emptyTemplate_returnsEmptyString() {
            mockFileReader.addFile("empty.html", "")
            val processor = TemplateProcessor(mockFileReader)

            val result = processor.processTemplate("empty.html")

            assertThat(result).isEqualTo("")
        }

        @Test
        fun processTemplate_whitespaceOnlyTemplate_returnsWhitespace() {
            mockFileReader.addFile("whitespace.html", "   \n\t  ")
            val processor = TemplateProcessor(mockFileReader)

            val result = processor.processTemplate("whitespace.html")

            assertThat(result).isEqualTo("   \n\t  ")
        }

        @Test
        fun processTemplate_templateWithoutPlaceholders_returnsUnchanged() {
            val content = "<html><body>Static content</body></html>"
            mockFileReader.addFile("static.html", content)
            val processor = TemplateProcessor(mockFileReader)

            val result = processor.processTemplate("static.html")

            assertThat(result).isEqualTo(content)
        }

        @Test
        fun processTemplate_malformedVariableSyntax_leavesUnchanged() {
            val template = "{{name} {name}} {{name"
            mockFileReader.addFile("malformed.html", template)
            val processor = TemplateProcessor(mockFileReader).withData("name", "Test")

            val result = processor.processTemplate("malformed.html")

            assertThat(result).isEqualTo(template)
        }

        @Test
        fun processTemplate_multipleVariablesInSameLine_processesCorrectly() {
            mockFileReader.addFile("multiple.html", "{{first}} and {{second}} and {{first}}")
            val processor =
                TemplateProcessor(mockFileReader)
                    .withData("first", "A")
                    .withData("second", "B")

            val result = processor.processTemplate("multiple.html")

            assertThat(result).isEqualTo("A and B and A")
        }

        @Test
        fun processTemplate_variableWithSpecialCharacters_processesCorrectly() {
            mockFileReader.addFile("special.html", "Value: {{special_var}}")
            val processor = TemplateProcessor(mockFileReader).withData("special_var", "Special!@#$%")

            val result = processor.processTemplate("special.html")

            assertThat(result).isEqualTo("Value: Special!@#$%")
        }

        @Test
        fun processTemplate_undefinedVariable_leavesPlaceholder() {
            mockFileReader.addFile("undefined.html", "Hello {{undefined}}!")
            val processor = TemplateProcessor(mockFileReader)

            val result = processor.processTemplate("undefined.html")

            assertThat(result).isEqualTo("Hello {{undefined}}!")
        }
    }

    @Nested
    inner class PerformanceAndLimits {
        @Test
        fun processTemplate_largeDataset_processesCorrectly() {
            val template = "{{for item in items}}{{item}},{{/for}}"
            mockFileReader.addFile("large.html", template)

            val largeList = (1..1000).map { "item$it" }
            val processor = TemplateProcessor(mockFileReader).withData("items", largeList)

            val result = processor.processTemplate("large.html")

            val expected = largeList.joinToString("") { "$it," }
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun processTemplate_deeplyNestedObjects_processesCorrectly() {
            data class Level3(
                val value: String,
            )

            data class Level2(
                val level3: Level3,
            )

            data class Level1(
                val level2: Level2,
            )

            val template = "{{for item in items}}{{item.level2.level3.value}}{{/for}}"
            mockFileReader.addFile("deep.html", template)

            val deepObjects =
                listOf(
                    Level1(Level2(Level3("Deep1"))),
                    Level1(Level2(Level3("Deep2"))),
                )
            val processor = TemplateProcessor(mockFileReader).withData("items", deepObjects)

            val result = processor.processTemplate("deep.html")

            assertThat(result).isEqualTo("Deep1Deep2")
        }

        @Test
        fun processTemplate_manyVariables_processesCorrectly() {
            val templateBuilder = StringBuilder()
            val processor = TemplateProcessor(mockFileReader)
            var processorWithData = processor

            for (i in 1..100) {
                templateBuilder.append("{{var$i}}")
                processorWithData = processorWithData.withData("var$i", "value$i")
            }

            mockFileReader.addFile("many_vars.html", templateBuilder.toString())

            val result = processorWithData.processTemplate("many_vars.html")

            val expected = (1..100).joinToString("") { "value$it" }
            assertThat(result).isEqualTo(expected)
        }
    }

    @Nested
    inner class IntegrationScenarios {
        @Test
        fun processTemplate_complexTemplateWithAllFeatures_processesCorrectly() {
            // Main template with includes, loops, and variables
            val mainTemplate =
                """
                <h1>{{title}}</h1>
                {{include:header.html}}
                <main>
                    {{for section in sections}}
                        {{include:section.html}}
                    {{/for}}
                </main>
                {{include:footer.html}}
                """.trimIndent()

            mockFileReader.addFile("main.html", mainTemplate)
            mockFileReader.addFile("header.html", "<header>Site Header</header>")
            mockFileReader.addFile("footer.html", "<footer>Site Footer</footer>")
            mockFileReader.addFile("section.html", "<section><h2>{{section.title}}</h2><p>{{section.content}}</p></section>")

            data class Section(
                val title: String,
                val content: String,
            )
            val sections =
                listOf(
                    Section("About", "About us content"),
                    Section("Contact", "Contact information"),
                )

            val processor =
                TemplateProcessor(mockFileReader)
                    .withData("title", "My Website")
                    .withData("sections", sections)

            val result = processor.processTemplate("main.html")

            val expected =
                """
                <h1>My Website</h1>
                <header>Site Header</header>
                <main>
                    <section><h2>About</h2><p>About us content</p></section><section><h2>Contact</h2><p>Contact information</p></section>
                </main>
                <footer>Site Footer</footer>
                """.trimIndent()

            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun processTemplate_includeFileWithLoopsAndVariables_processesCorrectly() {
            mockFileReader.addFile("main.html", "Welcome {{user}}! {{include:menu.html}}")
            mockFileReader.addFile("menu.html", "<ul>{{for item in menuItems}}<li>{{item}}</li>{{/for}}</ul>")

            val processor =
                TemplateProcessor(mockFileReader)
                    .withData("user", "John")
                    .withData("menuItems", listOf("Home", "About", "Contact"))

            val result = processor.processTemplate("main.html")

            assertThat(result).isEqualTo("Welcome John! <ul><li>Home</li><li>About</li><li>Contact</li></ul>")
        }
    }
}
