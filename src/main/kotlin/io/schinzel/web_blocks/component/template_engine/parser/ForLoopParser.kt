package io.schinzel.web_blocks.component.template_engine.parser

import io.schinzel.web_blocks.component.template_engine.ast.ForLoopNode
import io.schinzel.web_blocks.component.template_engine.ast.TagToken
import io.schinzel.web_blocks.component.template_engine.ast.TemplateNode
import io.schinzel.web_blocks.component.template_engine.ast.TextNode

/**
 * The purpose of this class is to parse for-loop constructs with
 * proper syntax validation and nested content parsing.
 *
 * Written by Claude Sonnet 4
 */
class ForLoopParser : NodeParser {
    companion object {
        private const val FOR_PREFIX = "for "
        private const val FOR_END_TAG = "/for"
        private val FOR_PATTERN = Regex("""for\s+(\w+)\s+in\s+(\w+(?:\.\w+)*)""")
    }

    /**
     * The purpose of this function is to check if tag content
     * represents a for-loop declaration and validate syntax.
     */
    override fun canParse(tagContent: String): Boolean {
        // Check if it's any variation of "for" syntax, including malformed ones
        if (tagContent.startsWith("for")) {
            // If it starts with "for " but doesn't match the pattern, it's invalid syntax
            if (tagContent.startsWith(FOR_PREFIX) && !FOR_PATTERN.matches(tagContent)) {
                throw IllegalArgumentException("Invalid loop syntax: $tagContent")
            }
            // If it's just "for" without space, also invalid
            if (tagContent == "for") {
                throw IllegalArgumentException("Invalid loop syntax: $tagContent")
            }
            // If it starts with "for " it's our responsibility to handle
            return tagContent.startsWith(FOR_PREFIX)
        }

        return false
    }

    /**
     * The purpose of this function is to parse for-loop syntax and
     * extract loop body using the main parser for nested content.
     */
    override fun parse(
        tagContent: String,
        tokenStream: TokenStream,
        mainParser: TemplateParser,
    ): TemplateNode {
        val matchResult =
            FOR_PATTERN.matchEntire(tagContent)
                ?: throw IllegalArgumentException("Invalid for-loop syntax: $tagContent")

        val variable = matchResult.groupValues[1]
        val list = matchResult.groupValues[2]

        // Parse the loop body until we find the closing tag
        val parseResult = mainParser.parseUntil(tokenStream, FOR_END_TAG)

        // If no closing tag was found, treat the entire construct as text
        if (!parseResult.stopTagFound) {
            // Reconstruct the original text by combining the tag with following content
            val bodyText =
                parseResult.nodes.joinToString("") { node ->
                    when (node) {
                        is TextNode -> node.text
                        else -> ""
                    }
                }
            return TextNode("{{$tagContent}}$bodyText", 0, 0)
        }

        // Get position from current token stream state for error reporting
        val currentToken = tokenStream.peek()
        val line = if (currentToken is TagToken) currentToken.line else 0
        val column = if (currentToken is TagToken) currentToken.column else 0

        return ForLoopNode(variable, list, parseResult.nodes, line, column)
    }
}
