package io.schinzel.web_blocks.component.template_engine.parser

import io.schinzel.web_blocks.component.template_engine.ast.IncludeNode
import io.schinzel.web_blocks.component.template_engine.ast.TagToken
import io.schinzel.web_blocks.component.template_engine.ast.TemplateNode

/**
 * The purpose of this class is to parse include directives with
 * filename validation and security checks.
 *
 * Written by Claude Sonnet 4
 */
class IncludeParser : NodeParser {
    companion object {
        private const val INCLUDE_PREFIX = "include:"
    }

    /**
     * The purpose of this function is to check if tag content
     * represents an include directive.
     */
    override fun canParse(tagContent: String): Boolean = tagContent.startsWith(INCLUDE_PREFIX)

    /**
     * The purpose of this function is to parse include directives
     * with filename extraction and security validation.
     */
    override fun parse(
        tagContent: String,
        tokenStream: TokenStream,
        mainParser: TemplateParser,
    ): TemplateNode {
        val fileName = tagContent.substring(INCLUDE_PREFIX.length).trim()

        // Security validation to prevent directory traversal
        require(fileName.isNotBlank()) { "Include filename cannot be blank" }
        require(!fileName.contains("..")) {
            "Path traversal not allowed in include filenames"
        }

        // Get position from current token stream state for error reporting
        val currentToken = tokenStream.peek()
        val line = if (currentToken is TagToken) currentToken.line else 0
        val column = if (currentToken is TagToken) currentToken.column else 0

        return IncludeNode(fileName, line, column)
    }
}
