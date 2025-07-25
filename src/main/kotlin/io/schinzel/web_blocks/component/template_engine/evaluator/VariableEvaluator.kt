package io.schinzel.web_blocks.component.template_engine.evaluator

import io.schinzel.web_blocks.component.template_engine.ast.VariableNode
import io.schinzel.web_blocks.component.template_engine.context.ProcessingContext

/**
 * The purpose of this class is to evaluate variable nodes by resolving
 * them from the processing context with property access support.
 *
 * Written by Claude Sonnet 4
 */
class VariableEvaluator : NodeEvaluator<VariableNode> {
    /**
     * The purpose of this function is to resolve variable references
     * from context with fallback to placeholder if undefined.
     */
    override fun evaluate(
        node: VariableNode,
        context: ProcessingContext,
        evaluator: TemplateEvaluator,
    ): String {
        val value = context.lookup(node.name)

        return when (value) {
            null -> {
                // Check if the property path is accessible (public property that's null)
                // vs inaccessible (private property or doesn't exist)
                if (context.isPropertyAccessible(node.name)) {
                    // Property exists and is accessible but is null -> render as empty string
                    ""
                } else {
                    // Property doesn't exist or is private -> keep placeholder
                    "{{${node.name}}}"
                }
            }
            else -> value.toString()
        }
    }
}
