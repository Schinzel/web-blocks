package io.schinzel.web_blocks.component.template_engine.evaluator

import io.schinzel.web_blocks.component.template_engine.ast.TextNode
import io.schinzel.web_blocks.component.template_engine.context.ProcessingContext

/**
 * The purpose of this class is to evaluate text nodes by returning
 * their content unchanged for static template content.
 *
 * Written by Claude Sonnet 4
 */
class TextNodeEvaluator : NodeEvaluator<TextNode> {
    /**
     * The purpose of this function is to return text content unchanged
     * since text nodes represent static template content.
     */
    override fun evaluate(
        node: TextNode,
        context: ProcessingContext,
        evaluator: TemplateEvaluator,
    ): String = node.text
}
