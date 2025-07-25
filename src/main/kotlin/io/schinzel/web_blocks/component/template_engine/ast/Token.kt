package io.schinzel.web_blocks.component.template_engine.ast

/**
 * The purpose of this sealed class is to define token types with position
 * tracking for precise error reporting and debugging capabilities.
 *
 * Written by Claude Sonnet 4
 */
sealed class Token(
    val line: Int,
    val column: Int,
)

/**
 * The purpose of this class is to represent static text content tokens
 * that will be rendered directly without any processing.
 *
 * Written by Claude Sonnet 4
 */
data class TextToken(
    val text: String,
    val textLine: Int,
    val textColumn: Int,
) : Token(textLine, textColumn)

/**
 * The purpose of this class is to represent template tag tokens
 * containing directives like variables, loops, or includes.
 *
 * Written by Claude Sonnet 4
 */
data class TagToken(
    val content: String,
    val tagLine: Int,
    val tagColumn: Int,
) : Token(tagLine, tagColumn)
