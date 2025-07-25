package io.schinzel.web_blocks.component.template_engine.parser

import io.schinzel.web_blocks.component.template_engine.ast.TemplateNode

/**
 * The purpose of this interface is to define the contract for parsing
 * specific template constructs with extensible parser architecture.
 *
 * Written by Claude Sonnet 4
 */
interface NodeParser {
    /**
     * The purpose of this function is to determine if this parser
     * can handle the given tag content type.
     */
    fun canParse(tagContent: String): Boolean

    /**
     * The purpose of this function is to parse tag content into appropriate
     * AST nodes with support for nested parsing through the main parser.
     */
    fun parse(
        tagContent: String,
        tokenStream: TokenStream,
        mainParser: TemplateParser,
    ): TemplateNode
}
