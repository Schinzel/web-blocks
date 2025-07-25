package io.schinzel.web_blocks.component.template_engine.evaluator

import io.schinzel.web_blocks.component.template_engine.ast.TemplateNode
import io.schinzel.web_blocks.component.template_engine.context.ProcessingContext
import kotlin.reflect.KClass

/**
 * The purpose of this class is to coordinate evaluation of AST nodes
 * using registered evaluators with type-safe dispatch.
 *
 * Written by Claude Sonnet 4
 */
class TemplateEvaluator(
    private val evaluators: Map<KClass<*>, NodeEvaluator<*>>,
) {
    /**
     * The purpose of this function is to evaluate a single AST node
     * by dispatching to the appropriate evaluator.
     */
    fun evaluate(
        node: TemplateNode,
        context: ProcessingContext,
    ): String {
        val evaluator =
            evaluators[node::class]
                ?: throw IllegalStateException("No evaluator for ${node::class.simpleName}")

        @Suppress("UNCHECKED_CAST")
        return (evaluator as NodeEvaluator<TemplateNode>).evaluate(node, context, this)
    }

    /**
     * The purpose of this function is to evaluate a list of AST nodes
     * and concatenate their results efficiently.
     */
    fun evaluateAll(
        nodes: List<TemplateNode>,
        context: ProcessingContext,
    ): String =
        nodes.joinToString("") { node ->
            evaluate(node, context)
        }
}
