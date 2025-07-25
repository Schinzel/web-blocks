package io.schinzel.web_blocks.component.template_engine.parser

import io.schinzel.web_blocks.component.template_engine.ast.TagToken
import io.schinzel.web_blocks.component.template_engine.ast.TemplateNode
import io.schinzel.web_blocks.component.template_engine.ast.VariableNode

/**
 * The purpose of this class is to parse variable references including
 * simple variables and property access with validation.
 *
 * Written by Claude Sonnet 4
 */
class VariableParser : NodeParser {
    companion object {
        // Matches variables like "name", "user.name", "item.property.subproperty"
        private val VARIABLE_PATTERN = Regex("""^\w+(?:\.\w+)*$""")
    }

    /**
     * The purpose of this function is to check if tag content
     * represents a variable reference.
     */
    override fun canParse(tagContent: String): Boolean = VARIABLE_PATTERN.matches(tagContent)

    /**
     * The purpose of this function is to parse variable references
     * with validation and position tracking for error reporting.
     */
    override fun parse(
        tagContent: String,
        tokenStream: TokenStream,
        mainParser: TemplateParser,
    ): TemplateNode {
        require(VARIABLE_PATTERN.matches(tagContent)) {
            "Invalid variable syntax: $tagContent"
        }

        // Get position from current token stream state for error reporting
        val currentToken = tokenStream.peek()
        val line = if (currentToken is TagToken) currentToken.line else 0
        val column = if (currentToken is TagToken) currentToken.column else 0

        return VariableNode(tagContent, line, column)
    }
}
