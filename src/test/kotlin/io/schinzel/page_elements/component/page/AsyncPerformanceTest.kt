package io.schinzel.page_elements.component.page

import io.schinzel.page_elements.component.page.PageBuilder
import io.schinzel.page_elements.component.page.IPageElement
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

/**
 * The purpose of this test class is to verify that async rendering
 * provides parallel execution performance benefits.
 * 
 * These tests prove that page elements render in parallel (not sequentially)
 * and that the async framework properly handles timeouts and error isolation.
 */
class AsyncPerformanceTest {

    /**
     * Simple test element that simulates 100ms processing time
     */
    class SlowPageElement(private val elementName: String) : IPageElement {
        override suspend fun getHtml(): String {
            // Simulate 100ms of work (e.g., database query, API call)
            delay(100)
            return "<div>$elementName content loaded in 100ms</div>"
        }
    }

    @Test
    fun `three 100ms elements render in parallel - total time around 100ms not 300ms`() {
        // Create a page with 3 elements that each take 100ms
        val pageBuilder = PageBuilder()
            .setTitle("Performance Test")
            .addRow()
            .addColumn(4)
            .addPageElement(SlowPageElement("Element1"))
            .addColumn(4) 
            .addPageElement(SlowPageElement("Element2"))
            .addColumn(4)
            .addPageElement(SlowPageElement("Element3"))

        // Measure total rendering time
        val totalTime = runBlocking {
            measureTimeMillis {
                val html = pageBuilder.getHtml()
                
                // Verify all elements are present in the output
                assertThat(html).contains("Element1 content loaded in 100ms")
                assertThat(html).contains("Element2 content loaded in 100ms") 
                assertThat(html).contains("Element3 content loaded in 100ms")
            }
        }

        // Assert parallel execution: should be ~100ms (parallel) not ~300ms (sequential)
        // Allow some overhead for coroutine scheduling and HTML generation
        assertThat(totalTime)
            .describedAs("Total rendering time should be around 100ms (parallel) not 300ms (sequential)")
            .isLessThan(200) // Allow 100ms overhead for safety
            .isGreaterThan(90) // Must be at least the time of one element
    }

    @Test
    fun `elements with different timeouts work correctly`() {
        // Element that takes longer than default timeout
        class TimeoutTestElement : IPageElement {
            override val timeoutMs: Long = 50 // Very short timeout
            
            override suspend fun getHtml(): String {
                delay(100) // Takes longer than timeout
                return "<div>This should timeout</div>"
            }
        }

        // Fast element that completes within timeout  
        class FastElement : IPageElement {
            override suspend fun getHtml(): String {
                delay(10) // Fast
                return "<div>Fast element</div>"
            }
        }

        val pageBuilder = PageBuilder()
            .setTitle("Timeout Test")
            .addRow()
            .addColumn(6)
            .addPageElement(TimeoutTestElement())
            .addColumn(6)
            .addPageElement(FastElement())

        val html = runBlocking {
            pageBuilder.getHtml()
        }

        // Verify timeout handling works
        assertThat(html).contains("Content loading too slow. Please try refreshing.")
        assertThat(html).contains("Fast element")
        assertThat(html).doesNotContain("This should timeout")
    }

    @Test
    fun `error isolation - one failing element does not affect others`() {
        // Element that throws an exception
        class FailingElement : IPageElement {
            override suspend fun getHtml(): String {
                throw RuntimeException("Simulated failure")
            }
        }

        // Working element
        class WorkingElement : IPageElement {
            override suspend fun getHtml(): String {
                return "<div>Working element</div>"
            }
        }

        val pageBuilder = PageBuilder()
            .setTitle("Error Isolation Test")
            .addRow()
            .addColumn(6)
            .addPageElement(FailingElement())
            .addColumn(6)
            .addPageElement(WorkingElement())

        val html = runBlocking {
            pageBuilder.getHtml()
        }

        // Verify error isolation works
        assertThat(html).contains("An unexpected error occurred: Simulated failure")
        assertThat(html).contains("Working element")
    }
}