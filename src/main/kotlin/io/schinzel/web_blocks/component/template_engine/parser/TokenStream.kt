package io.schinzel.web_blocks.component.template_engine.parser

import io.schinzel.web_blocks.component.template_engine.ast.Token

/**
 * The purpose of this class is to provide a stream-like interface
 * for consuming tokens during parsing with backtrack capabilities.
 *
 * Written by Claude Sonnet 4
 */
class TokenStream(
    private val tokens: List<Token>,
) {
    private var position = 0

    /**
     * The purpose of this function is to check if more tokens
     * are available for parsing without consuming them.
     */
    fun hasNext(): Boolean = position < tokens.size

    /**
     * The purpose of this function is to peek at the next token
     * without advancing the stream position.
     */
    fun peek(): Token? = if (hasNext()) tokens[position] else null

    /**
     * The purpose of this function is to consume and return the
     * next token from the stream.
     */
    fun next(): Token? = if (hasNext()) tokens[position++] else null

    /**
     * The purpose of this function is to get the current position
     * in the token stream for backtracking capabilities.
     */
    fun getPosition(): Int = position

    /**
     * The purpose of this function is to restore the stream position
     * to a previously saved state for backtracking parsing.
     */
    fun setPosition(newPosition: Int) {
        require(newPosition in 0..tokens.size) { "Invalid position: $newPosition" }
        position = newPosition
    }

    /**
     * The purpose of this function is to consume tokens until
     * finding a specific tag content for block parsing.
     */
    fun consumeUntil(stopTag: String): List<Token> {
        val consumedTokens = mutableListOf<Token>()

        while (hasNext()) {
            val token = peek()
            if (token is io.schinzel.web_blocks.component.template_engine.ast.TagToken &&
                token.content == stopTag
            ) {
                break
            }
            consumedTokens.add(next()!!)
        }

        return consumedTokens
    }
}
