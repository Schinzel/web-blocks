package io.schinzel.page_elements.web

import io.schinzel.basic_utils_kotlin.println

sealed class Environment {
    data object DEVELOPMENT : Environment()
    data object STAGING : Environment()
    data object PRODUCTION : Environment()

    fun isProduction() = this == PRODUCTION

    fun isDevelopment() = this == DEVELOPMENT
    fun isNotDevelopment() = !isDevelopment()

    fun getEnvironmentName() = this::class.simpleName?.lowercase()
        ?: throw IllegalStateException("Environment name not found")
}


/**
 * How to add a new environment
 */
data object ACCEPTANCE : Environment()


fun main() {
    Environment.DEVELOPMENT.getEnvironmentName().println()
    ACCEPTANCE.getEnvironmentName().println()
}