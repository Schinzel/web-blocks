package io.schinzel.page_elements.component.page


/**
 * The purpose of this interface is to represent a page-element.
 */
interface IPageElement {
    suspend fun getHtml(): String
    
    // Default timeout of 1 second to encourage snappy UIs
    // Elements can override for special cases (external APIs, etc.)
    val timeoutMs: Long
        get() = 1_000
}