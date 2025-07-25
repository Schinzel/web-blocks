package io.schinzel.web_blocks.component.template_engine.evaluator

import io.schinzel.web_blocks.component.template_engine.ast.TemplateNode
import io.schinzel.web_blocks.component.template_engine.context.ProcessingContext

/**
 * The purpose of this interface is to define the contract for evaluating
 * AST nodes into rendered string output with context support.
 *
 * Written by Claude Sonnet 4
 */
interface NodeEvaluator<T : TemplateNode> {
    /**
     * The purpose of this function is to evaluate a specific node type
     * within the given context and produce rendered string output.
     */
    fun evaluate(
        node: T,
        context: ProcessingContext,
        evaluator: TemplateEvaluator,
    ): String
}
