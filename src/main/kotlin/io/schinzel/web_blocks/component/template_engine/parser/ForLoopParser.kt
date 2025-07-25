package io.schinzel.web_blocks.component.template_engine.parser

import io.schinzel.web_blocks.component.template_engine.ast.ForLoopNode
import io.schinzel.web_blocks.component.template_engine.ast.TagToken
import io.schinzel.web_blocks.component.template_engine.ast.TemplateNode

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
        if (!tagContent.startsWith(FOR_PREFIX)) {
            return false
        }

        // If it starts with "for " but doesn't match the pattern, it's invalid syntax
        if (!FOR_PATTERN.matches(tagContent)) {
            throw IllegalArgumentException("Invalid loop syntax: $tagContent")
        }

        return true
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
        val bodyNodes = mainParser.parseUntil(tokenStream, FOR_END_TAG)

        // Get position from current token stream state for error reporting
        val currentToken = tokenStream.peek()
        val line = if (currentToken is TagToken) currentToken.line else 0
        val column = if (currentToken is TagToken) currentToken.column else 0

        return ForLoopNode(variable, list, bodyNodes, line, column)
    }
}
