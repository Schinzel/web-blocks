package io.schinzel.page_elements.component.page

import kotlinx.coroutines.*

/**
 * The purpose of this class is to represent a column in a web page.
 */
class Column(val span: Int) {
    val elements: MutableList<ObservablePageElement> = mutableListOf()

    suspend fun getHtml(): String = supervisorScope {
        // Use supervisorScope for error isolation at element level
        // If one element fails/times out, other elements continue rendering
        val elementsHtml = elements
            .map { element -> 
                async { 
                    try {
                        withTimeout(element.timeoutMs) { // Use element's timeout
                            element.getHtml() 
                        }
                    } catch (e: TimeoutCancellationException) {
                        "<div class='error timeout'>Content loading too slow. Please try refreshing.</div>"
                    } catch (e: Exception) {
                        "<div class='error exception'>An unexpected error occurred: ${e.message}</div>"
                    }
                }
            }
            .awaitAll()
            .joinToString("\n")
        
        """
        |<div class="col-$span">
        |  $elementsHtml
        |</div>
        """.trimMargin()
    }
}