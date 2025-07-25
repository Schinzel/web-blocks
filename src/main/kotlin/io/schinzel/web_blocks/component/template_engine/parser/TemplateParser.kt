package io.schinzel.web_blocks.component.template_engine.parser

import io.schinzel.web_blocks.component.template_engine.ast.TagToken
import io.schinzel.web_blocks.component.template_engine.ast.TemplateNode
import io.schinzel.web_blocks.component.template_engine.ast.TextNode
import io.schinzel.web_blocks.component.template_engine.ast.TextToken
import io.schinzel.web_blocks.component.template_engine.ast.Token

/**
 * The purpose of this class is to parse tokens into AST nodes using
 * pluggable parsers for different template constructs.
 *
 * Written by Claude Sonnet 4
 */
class TemplateParser(
    private val parsers: List<NodeParser>,
) {
    /**
     * The purpose of this function is to parse a list of tokens
     * into an AST using registered node parsers.
     */
    fun parse(tokens: List<Token>): List<TemplateNode> {
        val tokenStream = TokenStream(tokens)
        val nodes = mutableListOf<TemplateNode>()

        while (tokenStream.hasNext()) {
            val token = tokenStream.next()!!
            val node =
                when (token) {
                    is TextToken -> TextNode(token.text, token.line, token.column)
                    is TagToken -> parseTag(token, tokenStream)
                }
            nodes.add(node)
        }

        return nodes
    }

    /**
     * The purpose of this function is to parse tokens until encountering
     * a specific stop tag for block-based parsing like loops.
     */
    fun parseUntil(
        tokenStream: TokenStream,
        stopTag: String,
    ): ParseUntilResult {
        val nodes = mutableListOf<TemplateNode>()
        var stopTagFound = false

        while (tokenStream.hasNext()) {
            val token = tokenStream.peek()
            if (token is TagToken && token.content == stopTag) {
                // Consume the stop tag but don't include it in results
                tokenStream.next()
                stopTagFound = true
                break
            }

            val nextToken = tokenStream.next()!!
            val node =
                when (nextToken) {
                    is TextToken -> TextNode(nextToken.text, nextToken.line, nextToken.column)
                    is TagToken -> parseTag(nextToken, tokenStream)
                }
            nodes.add(node)
        }

        return ParseUntilResult(nodes, stopTagFound)
    }

    data class ParseUntilResult(
        val nodes: List<TemplateNode>,
        val stopTagFound: Boolean,
    )

    /**
     * The purpose of this function is to delegate tag parsing to
     * appropriate specialized parsers based on tag content.
     */
    private fun parseTag(
        tagToken: TagToken,
        tokenStream: TokenStream,
    ): TemplateNode {
        for (parser in parsers) {
            if (parser.canParse(tagToken.content)) {
                return parser.parse(tagToken.content, tokenStream, this)
            }
        }

        // If no parser can handle it, treat as text (malformed tag)
        return TextNode("{{${tagToken.content}}}", tagToken.line, tagToken.column)
    }
}
