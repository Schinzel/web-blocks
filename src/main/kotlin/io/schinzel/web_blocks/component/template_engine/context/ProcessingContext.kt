package io.schinzel.web_blocks.component.template_engine.context

/**
 * The purpose of this class is to provide immutable, layered context
 * for variable resolution with proper scoping for nested constructs.
 *
 * Written by Claude Sonnet 4
 */
class ProcessingContext(
    private val data: Map<String, Any> = emptyMap(),
    private val parent: ProcessingContext? = null,
) {
    companion object {
        // Sentinel value to distinguish between null property values and missing properties
        private val PROPERTY_NOT_FOUND = Any()
    }

    /**
     * The purpose of this function is to look up variables with
     * proper scope chain resolution from current to parent contexts.
     */
    fun lookup(name: String): Any? {
        // First try to resolve property access like "user.name"
        if (name.contains('.')) {
            val result = resolvePropertyPath(name)
            if (result !== null && result !== PROPERTY_NOT_FOUND) return result
            if (result === PROPERTY_NOT_FOUND) return null // Property doesn't exist or is private
        }

        // Then try direct lookup in current context
        val directResult = data[name]
        if (directResult != null) return directResult

        // Finally try parent context
        return parent?.lookup(name)
    }

    /**
     * The purpose of this function is to check if a property path exists
     * and is accessible, for VariableEvaluator to distinguish between
     * null values and missing/private properties.
     */
    fun isPropertyAccessible(name: String): Boolean {
        if (!name.contains('.')) {
            return data.containsKey(name) || parent?.isPropertyAccessible(name) == true
        }

        val result = resolvePropertyPath(name)
        return result !== PROPERTY_NOT_FOUND
    }

    /**
     * The purpose of this function is to create a new context layer
     * with additional data while preserving immutability.
     */
    fun with(
        key: String,
        value: Any?,
    ): ProcessingContext {
        val nonNullValue = value ?: ""
        return ProcessingContext(mapOf(key to nonNullValue), parent = this)
    }

    /**
     * The purpose of this function is to create a new context layer
     * with multiple data entries for bulk updates.
     */
    fun withAll(newData: Map<String, Any>): ProcessingContext = ProcessingContext(newData, parent = this)

    /**
     * The purpose of this function is to resolve property paths like
     * "user.name" or "item.properties.value" using reflection safely.
     */
    private fun resolvePropertyPath(path: String): Any? {
        val parts = path.split('.')
        if (parts.isEmpty()) return null

        // Start with the root object
        var current = data[parts[0]] ?: parent?.lookup(parts[0])
        if (current == null) return null

        // Navigate through the property chain
        for (i in 1 until parts.size) {
            val propertyResult = getProperty(current!!, parts[i])

            // If property doesn't exist (reflection failed), return sentinel value
            if (propertyResult === PROPERTY_NOT_FOUND) {
                return PROPERTY_NOT_FOUND
            }

            // If property exists but is null, continue with null (will be handled by evaluator)
            current = propertyResult
        }

        return current
    }

    /**
     * The purpose of this function is to safely extract property values
     * using reflection with enterprise security controls.
     */
    private fun getProperty(
        obj: Any,
        propertyName: String,
    ): Any? =
        try {
            // Use Kotlin reflection for safe property access
            val kClass = obj::class
            val property = kClass.members.find { it.name == propertyName }

            // Only access public properties for security
            if (property != null &&
                (property.visibility == null || property.visibility.toString() == "PUBLIC")
            ) {
                // Return the actual property value (including null)
                property.call(obj)
            } else {
                // Property doesn't exist or isn't accessible
                PROPERTY_NOT_FOUND
            }
        } catch (e: Exception) {
            // Graceful degradation if reflection fails
            PROPERTY_NOT_FOUND
        }

    /**
     * The purpose of this function is to provide debugging information
     * about the current context state for troubleshooting.
     */
    fun debugInfo(): String {
        val currentKeys = data.keys.toList()
        val parentInfo = parent?.debugInfo() ?: "none"
        return "Context(keys=$currentKeys, parent=$parentInfo)"
    }
}
