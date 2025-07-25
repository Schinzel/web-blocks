package io.schinzel.web_blocks.component.template_engine

import io.schinzel.web_blocks.component.template_engine.ast.ForLoopNode
import io.schinzel.web_blocks.component.template_engine.ast.IncludeNode
import io.schinzel.web_blocks.component.template_engine.ast.TextNode
import io.schinzel.web_blocks.component.template_engine.ast.Tokenizer
import io.schinzel.web_blocks.component.template_engine.ast.VariableNode
import io.schinzel.web_blocks.component.template_engine.context.ProcessingContext
import io.schinzel.web_blocks.component.template_engine.evaluator.ForLoopEvaluator
import io.schinzel.web_blocks.component.template_engine.evaluator.IncludeEvaluator
import io.schinzel.web_blocks.component.template_engine.evaluator.NodeEvaluator
import io.schinzel.web_blocks.component.template_engine.evaluator.TemplateEvaluator
import io.schinzel.web_blocks.component.template_engine.evaluator.TextNodeEvaluator
import io.schinzel.web_blocks.component.template_engine.evaluator.VariableEvaluator
import io.schinzel.web_blocks.component.template_engine.file_reader.FileReaderFactory
import io.schinzel.web_blocks.component.template_engine.file_reader.IFileReader
import io.schinzel.web_blocks.component.template_engine.parser.ForLoopParser
import io.schinzel.web_blocks.component.template_engine.parser.IncludeParser
import io.schinzel.web_blocks.component.template_engine.parser.TemplateParser
import io.schinzel.web_blocks.component.template_engine.parser.VariableParser
import kotlin.reflect.KClass

/**
 * The purpose of this class is to process templates using AST-based approach
 * with support for variables, includes, and loops with proper nesting and
 * immutable design principles for enterprise production environments.
 *
 * Written by Claude Sonnet 4
 */
class TemplateProcessor private constructor(
    private val fileReader: IFileReader,
    private val tokenizer: Tokenizer,
    private val parser: TemplateParser,
    private val evaluator: TemplateEvaluator,
    // Immutable data collections ensure thread safety
    private val stringData: Map<String, String>,
    private val listData: Map<String, List<Any>>,
) {
    constructor(caller: Any) : this(
        FileReaderFactory.create(caller),
        createTokenizer(),
        createParser(),
        createEvaluator(FileReaderFactory.create(caller)),
        emptyMap(),
        emptyMap(),
    )

    constructor(fileReader: IFileReader) : this(
        fileReader,
        createTokenizer(),
        createParser(),
        createEvaluator(fileReader),
        emptyMap(),
        emptyMap(),
    )

    /**
     * The purpose of this function is to create a new processor instance with
     * additional string or list data while maintaining immutability.
     */
    fun withData(
        key: String,
        value: Any,
    ): TemplateProcessor =
        when (value) {
            is String -> withStringData(key, value)
            is Int -> withStringData(key, value.toString())
            is List<*> -> withListData(key, value as List<Any>)
            else -> throw IllegalArgumentException("Unsupported data type: ${value::class}")
        }

    /**
     * The purpose of this function is to process a template file using
     * AST-based parsing and evaluation for superior performance and extensibility.
     */
    fun processTemplate(fileName: String): String {
        // Input validation prevents directory traversal and malformed paths
        require(fileName.isNotBlank()) { "Template file name cannot be blank" }
        require(!fileName.contains("..")) { "Path traversal not allowed in template names" }

        val content = fileReader.getFileContent(fileName)
        return processTemplateContent(content, createProcessingContext())
    }

    /**
     * The purpose of this function is to process template content with
     * a given context for include evaluation and nested processing.
     */
    fun processTemplateContent(
        content: String,
        context: ProcessingContext,
    ): String {
        // Tokenize the template content
        val tokens = tokenizer.tokenize(content)

        // Parse tokens into AST
        val ast = parser.parse(tokens)

        // Evaluate AST with context
        return evaluator.evaluateAll(ast, context)
    }

    companion object {
        /**
         * The purpose of this function is to create a tokenizer instance
         * for converting template strings into tokens.
         */
        private fun createTokenizer(): Tokenizer = Tokenizer()

        /**
         * The purpose of this function is to create a parser with all
         * registered node parsers for template constructs.
         */
        private fun createParser(): TemplateParser {
            val parsers =
                listOf(
                    ForLoopParser(),
                    IncludeParser(),
                    VariableParser(), // Variable parser should be last (catch-all)
                )
            return TemplateParser(parsers)
        }

        /**
         * The purpose of this function is to create an evaluator with all
         * registered node evaluators for AST node types.
         */
        private fun createEvaluator(fileReader: IFileReader): TemplateEvaluator {
            val evaluators = mutableMapOf<KClass<*>, NodeEvaluator<*>>()

            evaluators[TextNode::class] = TextNodeEvaluator()
            evaluators[VariableNode::class] = VariableEvaluator()
            evaluators[ForLoopNode::class] = ForLoopEvaluator()
            evaluators[IncludeNode::class] = IncludeEvaluator(fileReader)

            return TemplateEvaluator(evaluators)
        }
    }

    // Immutable data management - creates new instances instead of mutating state
    private fun withStringData(
        key: String,
        value: String,
    ): TemplateProcessor =
        TemplateProcessor(
            fileReader,
            tokenizer,
            parser,
            evaluator,
            stringData + (key to value),
            listData,
        )

    private fun withListData(
        key: String,
        value: List<Any>,
    ): TemplateProcessor =
        TemplateProcessor(
            fileReader,
            tokenizer,
            parser,
            evaluator,
            stringData,
            listData + (key to value),
        )

    /**
     * The purpose of this function is to create a processing context
     * with all current string and list data for template evaluation.
     */
    private fun createProcessingContext(): ProcessingContext {
        val allData = stringData + listData
        return ProcessingContext(allData)
    }

    /**
     * The purpose of this function is to provide backward compatibility
     * for any existing tests that call the old processLoops method directly.
     *
     * @deprecated Use processTemplate instead - loops are now processed as part of AST evaluation
     */
    @Deprecated("Use processTemplate instead - loops are now processed as part of AST evaluation")
    fun processLoops(template: String): String {
        // For backward compatibility, process the template with AST approach
        return processTemplateContent(template, createProcessingContext())
    }

    /**
     * The purpose of this function is to provide backward compatibility
     * for any existing tests that call the old applyStringData method directly.
     *
     * @deprecated Use processTemplate instead - variable substitution is now part of AST evaluation
     */
    @Deprecated("Use processTemplate instead - variable substitution is now part of AST evaluation")
    fun applyStringData(template: String): String {
        // For backward compatibility, process the template with AST approach
        return processTemplateContent(template, createProcessingContext())
    }
}
