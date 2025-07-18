package io.schinzel.web_blocks.component.page

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

/**
 * The purpose of this test class is to verify that async rendering
 * provides parallel execution performance benefits.
 *
 * These tests prove that blocks render in parallel (not sequentially)
 * and that the async framework properly handles timeouts and error isolation.
 */
class AsyncPerformanceTest {
    /**
     * Simple test block that simulates 100ms processing time
     */
    class SlowBlock(
        private val blockName: String,
    ) : IBlock {
        override suspend fun getHtml(): String {
            // Simulate 100ms of work (e.g., database query, API call)
            delay(100)
            return "<div>$blockName content loaded in 100ms</div>"
        }
    }

    @Nested
    inner class GetHtml {
        @Test
        fun `three slow blocks _ renders in parallel not sequential`() {
            // Create a page with 3 blocks that each take 100ms
            val pageBuilder =
                PageBuilder()
                    .setTitle("Performance Test")
                    .addRow()
                    .addColumn(4)
                    .addBlock(SlowBlock("Block1"))
                    .addColumn(4)
                    .addBlock(SlowBlock("Block2"))
                    .addColumn(4)
                    .addBlock(SlowBlock("Block3"))

            // Measure total rendering time
            val totalTime =
                runBlocking {
                    measureTimeMillis {
                        val html = pageBuilder.getHtml()

                        // Verify all blocks are present in the output
                        assertThat(html).contains("Block1 content loaded in 100ms")
                        assertThat(html).contains("Block2 content loaded in 100ms")
                        assertThat(html).contains("Block3 content loaded in 100ms")
                    }
                }

            // Assert parallel execution: should be ~100ms (parallel) not ~300ms (sequential)
            // Allow some overhead for coroutine scheduling and HTML generation
            assertThat(totalTime)
                .describedAs("Total rendering time should be around 100ms (parallel) not 300ms (sequential)")
                .isLessThan(200) // Allow 100ms overhead for safety
                .isGreaterThan(90) // Must be at least the time of one block
        }

        @Test
        fun `blocks with different timeouts _ handles timeouts correctly`() {
            // Block that takes longer than default timeout
            class TimeoutTestBlock : IBlock {
                override val timeoutMs: Long = 50 // Very short timeout

                override suspend fun getHtml(): String {
                    delay(100) // Takes longer than timeout
                    return "<div>This should timeout</div>"
                }
            }

            // Fast block that completes within timeout
            class FastBlock : IBlock {
                override suspend fun getHtml(): String {
                    delay(10) // Fast
                    return "<div>Fast block</div>"
                }
            }

            val pageBuilder =
                PageBuilder()
                    .setTitle("Timeout Test")
                    .addRow()
                    .addColumn(6)
                    .addBlock(TimeoutTestBlock())
                    .addColumn(6)
                    .addBlock(FastBlock())

            val html =
                runBlocking {
                    pageBuilder.getHtml()
                }

            // Verify timeout handling works
            assertThat(html).contains("Content loading too slow. Please try refreshing.")
            assertThat(html).contains("Fast block")
            assertThat(html).doesNotContain("This should timeout")
        }

        @Test
        fun `one failing block _ isolates errors`() {
            // Block that throws an exception
            class FailingBlock : IBlock {
                override suspend fun getHtml(): String = throw RuntimeException("Simulated failure")
            }

            // Working block
            class WorkingBlock : IBlock {
                override suspend fun getHtml(): String = "<div>Working block</div>"
            }

            val pageBuilder =
                PageBuilder()
                    .setTitle("Error Isolation Test")
                    .addRow()
                    .addColumn(6)
                    .addBlock(FailingBlock())
                    .addColumn(6)
                    .addBlock(WorkingBlock())

            val html =
                runBlocking {
                    pageBuilder.getHtml()
                }

            // Verify error isolation works
            assertThat(html).contains("An unexpected error occurred: Simulated failure")
            assertThat(html).contains("Working block")
        }
    }
}
