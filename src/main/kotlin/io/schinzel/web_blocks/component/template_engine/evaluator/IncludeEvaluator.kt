package io.schinzel.web_blocks.component.template_engine.evaluator

import io.schinzel.web_blocks.component.template_engine.ast.IncludeNode
import io.schinzel.web_blocks.component.template_engine.ast.Tokenizer
import io.schinzel.web_blocks.component.template_engine.context.ProcessingContext
import io.schinzel.web_blocks.component.template_engine.file_reader.IFileReader
import io.schinzel.web_blocks.component.template_engine.parser.ForLoopParser
import io.schinzel.web_blocks.component.template_engine.parser.IncludeParser
import io.schinzel.web_blocks.component.template_engine.parser.TemplateParser
import io.schinzel.web_blocks.component.template_engine.parser.VariableParser

/**
 * The purpose of this class is to evaluate include nodes by processing
 * included files with proper circular dependency protection.
 *
 * Written by Claude Sonnet 4
 */
class IncludeEvaluator(
    private val fileReader: IFileReader,
) : NodeEvaluator<IncludeNode> {
    companion object {
        private const val MAX_INCLUDE_DEPTH = 10
    }

    private val includeStack = mutableListOf<String>()

    /**
     * The purpose of this function is to evaluate include nodes by
     * processing included files with recursion protection.
     */
    override fun evaluate(
        node: IncludeNode,
        context: ProcessingContext,
        evaluator: TemplateEvaluator,
    ): String {
        // Check for circular dependencies
        if (includeStack.contains(node.fileName)) {
            throw IllegalStateException("Circular include dependency detected: ${includeStack + node.fileName}")
        }

        // Check maximum include depth
        if (includeStack.size >= MAX_INCLUDE_DEPTH) {
            throw IllegalStateException(
                "Max include depth ($MAX_INCLUDE_DEPTH) exceeded. Possible circular dependency.",
            )
        }

        return try {
            includeStack.add(node.fileName)

            // Read and process the included file using new parser/evaluator instances
            val includeContent = fileReader.getFileContent(node.fileName)

            // Create new tokenizer and parser for the included content
            val tokenizer = Tokenizer()
            val parsers = listOf(ForLoopParser(), IncludeParser(), VariableParser())
            val parser = TemplateParser(parsers)

            // Parse and evaluate the included content
            val tokens = tokenizer.tokenize(includeContent)
            val ast = parser.parse(tokens)
            evaluator.evaluateAll(ast, context)
        } finally {
            includeStack.removeLastOrNull()
        }
    }
}
