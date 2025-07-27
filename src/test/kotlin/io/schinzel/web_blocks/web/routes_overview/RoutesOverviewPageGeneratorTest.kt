package io.schinzel.web_blocks.web.routes_overview

import io.schinzel.web_blocks.web.route_mapping.RouteMapping
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test the RoutesOverviewPageGenerator
 * ensuring it correctly generates HTML for various route configurations.
 *
 * Written by Claude Sonnet 4
 */
class RoutesOverviewPageGeneratorTest {
    private val generator = RoutesOverviewPageGenerator()

    @Test
    fun generateHtml_noRoutes_returnsEmptyPageStructure() {
        // Given
        val emptyRoutes = emptyList<RouteMapping>()

        // When
        val html = generator.generateHtml(emptyRoutes)

        // Then
        assertThat(html).contains("WebBlocks Routes Overview")
        assertThat(html).contains("Pages &amp; Components")
        assertThat(html).contains("API Routes")
        assertThat(html).contains("</html>")
        assertThat(html).contains("<!DOCTYPE html>")
    }

    @Test
    fun generateHtml_emptyRoutes_containsBasicHtmlStructure() {
        // Given
        val emptyRoutes = emptyList<RouteMapping>()

        // When
        val html = generator.generateHtml(emptyRoutes)

        // Then
        assertThat(html).contains("<html>")
        assertThat(html).contains("<head>")
        assertThat(html).contains("<title>WebBlocks Routes Overview</title>")
        assertThat(html).contains("<body>")
        assertThat(html).contains("<h1>WebBlocks Routes Overview</h1>")
        assertThat(html).contains("<h2>Pages &amp; Components</h2>")
        assertThat(html).contains("<h2>API Routes</h2>")
        assertThat(html).contains("</body>")
        assertThat(html).contains("</html>")
    }

    @Test
    fun generateHtml_emptyRoutes_containsStyles() {
        // Given
        val emptyRoutes = emptyList<RouteMapping>()

        // When
        val html = generator.generateHtml(emptyRoutes)

        // Then
        assertThat(html).contains("<style>")
        assertThat(html).contains("font-family: monospace")
        assertThat(html).contains(".route")
        assertThat(html).contains(".route-path")
        assertThat(html).contains(".route-class")
        assertThat(html).contains(".route-file")
        assertThat(html).contains(".route-params")
        assertThat(html).contains("</style>")
    }

    @Test
    fun generateHtml_emptyRoutes_doesNotContainRouteSpecificContent() {
        // Given
        val emptyRoutes = emptyList<RouteMapping>()

        // When
        val html = generator.generateHtml(emptyRoutes)

        // Then
        assertThat(html).doesNotContain("Class:")
        assertThat(html).doesNotContain("File:")
        assertThat(html).doesNotContain("Parameters:")
        assertThat(html).doesNotContain("Blocks:")
        assertThat(html).doesNotContain("Block APIs:")
    }

    @Test
    fun generateHtml_emptyRoutes_sectionsAreEmpty() {
        // Given
        val emptyRoutes = emptyList<RouteMapping>()

        // When
        val html = generator.generateHtml(emptyRoutes)

        // Then
        val pagesSection =
            html
                .substringAfter("Pages &amp; Components")
                .substringBefore("API Routes")
        val apiSection =
            html
                .substringAfter("API Routes")
                .substringBefore("</body>")

        // Should not contain any route divs
        assertThat(pagesSection).doesNotContain("<div class=\"route\"")
        assertThat(apiSection).doesNotContain("<div class=\"route\"")
    }

    @Test
    fun generateHtml_correctHtmlEscaping() {
        // Given
        val emptyRoutes = emptyList<RouteMapping>()

        // When
        val html = generator.generateHtml(emptyRoutes)

        // Then
        // Verify HTML entities are properly escaped
        assertThat(html).contains("Pages &amp; Components")
        assertThat(html).doesNotContain("Pages & Components")
    }
}
