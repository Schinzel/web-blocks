package io.schinzel.web_blocks.component.template_engine.ast

/**
 * The purpose of this class is to convert template strings into tokens
 * with precise position tracking for comprehensive error reporting.
 *
 * Written by Claude Sonnet 4
 */
class Tokenizer {
    companion object {
        private const val TAG_START = "{{"
        private const val TAG_END = "}}"
    }

    /**
     * The purpose of this function is to tokenize template content into
     * text and tag tokens with line and column position tracking.
     */
    fun tokenize(content: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var currentLine = 1
        var currentColumn = 1
        var position = 0

        while (position < content.length) {
            val tagStartIndex = content.indexOf(TAG_START, position)

            if (tagStartIndex == -1) {
                // No more tags found, rest is text
                val remainingText = content.substring(position)
                if (remainingText.isNotEmpty()) {
                    tokens.add(TextToken(remainingText, currentLine, currentColumn))
                }
                break
            }

            // Add text before the tag if it exists
            if (tagStartIndex > position) {
                val textContent = content.substring(position, tagStartIndex)
                tokens.add(TextToken(textContent, currentLine, currentColumn))

                // Update position tracking based on text content
                val positionUpdate = updatePosition(textContent, currentLine, currentColumn)
                currentLine = positionUpdate.first
                currentColumn = positionUpdate.second
            }

            // Find the end of the tag
            val tagEndIndex = content.indexOf(TAG_END, tagStartIndex + TAG_START.length)
            if (tagEndIndex == -1) {
                // Unclosed tag, treat as text
                val remainingText = content.substring(tagStartIndex)
                tokens.add(TextToken(remainingText, currentLine, currentColumn))
                break
            }

            // Extract tag content (without the braces)
            val tagContent =
                content
                    .substring(
                        tagStartIndex + TAG_START.length,
                        tagEndIndex,
                    ).trim()

            tokens.add(TagToken(tagContent, currentLine, currentColumn))

            // Update position tracking for the entire tag
            val fullTag = content.substring(tagStartIndex, tagEndIndex + TAG_END.length)
            val positionUpdate = updatePosition(fullTag, currentLine, currentColumn)
            currentLine = positionUpdate.first
            currentColumn = positionUpdate.second

            position = tagEndIndex + TAG_END.length
        }

        return tokens
    }

    /**
     * The purpose of this function is to update line and column positions
     * based on processed content for accurate error reporting.
     */
    private fun updatePosition(
        content: String,
        startLine: Int,
        startColumn: Int,
    ): Pair<Int, Int> {
        var line = startLine
        var column = startColumn

        for (char in content) {
            when (char) {
                '\n' -> {
                    line++
                    column = 1
                }
                else -> column++
            }
        }

        return Pair(line, column)
    }
}
