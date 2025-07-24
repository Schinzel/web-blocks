package io.schinzel.web_blocks.component.page_builder

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withTimeout

/**
 * The purpose of this class is to represent a column in a web page.
 */
class Column(
    val span: Int,
) {
    val blocks: MutableList<IBlock> = mutableListOf()

    suspend fun getHtml(): String =
        supervisorScope {
            // Use supervisorScope for error isolation at block level
            // If one block fails/times out, other blocks continue rendering
            val blocksHtml =
                blocks
                    .map { block ->
                        async {
                            try {
                                withTimeout(block.timeoutMs) {
                                    // Use block's timeout
                                    block.getHtml()
                                }
                            } catch (e: TimeoutCancellationException) {
                                "<div class='error timeout'>Content loading too slow. Please try refreshing.</div>"
                            } catch (e: Exception) {
                                "<div class='error exception'>An unexpected error occurred: ${e.message}</div>"
                            }
                        }
                    }.awaitAll()
                    .joinToString("\n")

            """
        |<div class="col-$span">
        |  $blocksHtml
        |</div>
            """.trimMargin()
        }
}
