package io.schinzel.web_blocks.component.template_engine.evaluator

import io.schinzel.web_blocks.component.template_engine.ast.ForLoopNode
import io.schinzel.web_blocks.component.template_engine.context.ProcessingContext

/**
 * The purpose of this class is to evaluate for-loop nodes with proper
 * scoping and iteration over collections with nested content support.
 *
 * Written by Claude Sonnet 4
 */
class ForLoopEvaluator : NodeEvaluator<ForLoopNode> {
    /**
     * The purpose of this function is to evaluate for-loops by iterating
     * over collections with proper variable scoping for loop bodies.
     */
    override fun evaluate(
        node: ForLoopNode,
        context: ProcessingContext,
        evaluator: TemplateEvaluator,
    ): String {
        // Check for excessive loop operations before processing
        evaluator.checkLoopOperationLimit()

        val collection = context.lookup(node.list)

        if (collection !is List<*>) {
            // If the list doesn't exist or isn't a list, return empty string
            return ""
        }

        return collection.joinToString("") { item ->
            // Create new context with loop variable in scope
            val loopContext = context.with(node.variable, item ?: "")

            // Add object properties as list data for nested loops
            val contextWithProperties =
                if (shouldProcessProperties(item)) {
                    addObjectPropertiesToContext(loopContext, item!!)
                } else {
                    loopContext
                }

            // Evaluate loop body with the enriched context and trim whitespace
            val bodyResult = evaluator.evaluateAll(node.body, contextWithProperties)
            trimLoopBodyWhitespace(bodyResult)
        }
    }

    /**
     * The purpose of this function is to determine if an object should
     * have its properties processed for nested loop access.
     */
    private fun shouldProcessProperties(item: Any?): Boolean =
        item != null && item !is String && item !is Number && item !is Boolean

    /**
     * The purpose of this function is to add object properties as list data
     * to the context for supporting nested loops over object properties.
     */
    private fun addObjectPropertiesToContext(
        context: ProcessingContext,
        item: Any,
    ): ProcessingContext {
        var enrichedContext = context

        try {
            val properties = item::class.members
            for (property in properties) {
                // Only process public properties for security
                if (property.visibility == null || property.visibility.toString() == "PUBLIC") {
                    val propertyValue = property.call(item)
                    if (propertyValue is List<*>) {
                        @Suppress("UNCHECKED_CAST")
                        enrichedContext =
                            enrichedContext.with(
                                property.name,
                                propertyValue as List<Any>,
                            )
                    }
                }
            }
        } catch (_: Exception) {
            // Graceful degradation if reflection fails
        }

        return enrichedContext
    }

    /**
     * The purpose of this function is to trim leading and trailing
     * whitespace from loop body content to avoid formatting issues.
     */
    private fun trimLoopBodyWhitespace(content: String): String {
        // Remove leading whitespace (spaces, tabs, newlines) at the start
        // and trailing whitespace at the end, but preserve internal whitespace
        return content.trim()
    }
}
