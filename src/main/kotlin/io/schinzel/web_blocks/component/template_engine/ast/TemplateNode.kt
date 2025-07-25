package io.schinzel.web_blocks.component.template_engine.ast

/**
 * The purpose of this sealed class is to define the AST node hierarchy
 * for template processing with position tracking for superior error reporting.
 *
 * Written by Claude Sonnet 4
 */
sealed class TemplateNode(
    val line: Int = 0,
    val column: Int = 0,
)

/**
 * The purpose of this class is to represent static text content
 * that should be rendered as-is without any processing.
 *
 * Written by Claude Sonnet 4
 */
data class TextNode(
    val text: String,
    val textLine: Int = 0,
    val textColumn: Int = 0,
) : TemplateNode(textLine, textColumn)

/**
 * The purpose of this class is to represent variable references
 * that need to be resolved from the processing context.
 *
 * Written by Claude Sonnet 4
 */
data class VariableNode(
    val name: String,
    val variableLine: Int = 0,
    val variableColumn: Int = 0,
) : TemplateNode(variableLine, variableColumn)

/**
 * The purpose of this class is to represent for-loop constructs
 * with proper scoping for iteration variables and nested content.
 *
 * Written by Claude Sonnet 4
 */
data class ForLoopNode(
    val variable: String,
    val list: String,
    val body: List<TemplateNode>,
    val loopLine: Int = 0,
    val loopColumn: Int = 0,
) : TemplateNode(loopLine, loopColumn)

/**
 * The purpose of this class is to represent file inclusion directives
 * with circular dependency protection and relative path resolution.
 *
 * Written by Claude Sonnet 4
 */
data class IncludeNode(
    val fileName: String,
    val includeLine: Int = 0,
    val includeColumn: Int = 0,
) : TemplateNode(includeLine, includeColumn)
