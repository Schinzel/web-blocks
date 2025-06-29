package io.schinzel.web_blocks.component.page


/**
 * The purpose of this interface is to represent a block.
 */
interface IBlock {
    suspend fun getHtml(): String
    
    // Default timeout of 1 second to encourage snappy UIs
    // Blocks can override for special cases (external APIs, etc.)
    val timeoutMs: Long
        get() = 1_000
}