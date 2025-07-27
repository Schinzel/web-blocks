package io.schinzel.web_blocks.web.routes_overview

import io.javalin.Javalin
import io.schinzel.web_blocks.web.WebAppConfig
import io.schinzel.web_blocks.web.set_up_routes.setUpRoutes
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection
import java.net.URI

/**
 * The purpose of this class is to test the routes overview endpoint integration
 * using the sample app to ensure it works in a real environment.
 *
 * Written by Claude Sonnet 4
 */
class RoutesOverviewIntegrationTest {
    private lateinit var javalin: Javalin
    private val testPort = 7888
    private val baseUrl = "http://localhost:$testPort"

    @BeforeEach
    fun setUp() {
        // Use the sample app configuration to test with real routes
        val webAppConfig =
            WebAppConfig(
                webRootClass = io.schinzel.sample.MyWebApp(),
            )

        javalin = setUpRoutes(webAppConfig)
        javalin.start(testPort)

        // Give server time to start
        Thread.sleep(300)
    }

    @AfterEach
    fun tearDown() {
        javalin.stop()
        Thread.sleep(100)
    }

    @Test
    fun webBlocksRoutesEndpoint_returns200() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val responseCode = connection.responseCode

        // Then
        assertThat(responseCode).isEqualTo(200)
    }

    @Test
    fun webBlocksRoutesEndpoint_containsBasicStructure() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val html = connection.inputStream.bufferedReader().use { it.readText() }

        // Then
        assertThat(html).contains("WebBlocks Routes Overview")
        assertThat(html).contains("Pages &amp; Components")
        assertThat(html).contains("API Routes")
        assertThat(html).contains("<!DOCTYPE html>")
        assertThat(html).contains("</html>")
    }

    @Test
    fun webBlocksRoutesEndpoint_displaysSampleAppRoutes() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val html = connection.inputStream.bufferedReader().use { it.readText() }

        // Then
        // Should contain actual routes from the sample app
        assertThat(html).contains("/api/user-pets")
        assertThat(html).contains("UserPets")
        assertThat(html).contains("/simple-page")
        assertThat(html).contains("Page")

        // Should contain file paths for AI navigation
        assertThat(html).contains("src/main/kotlin/io/schinzel/sample/api/UserPets.kt")
        assertThat(html).contains("src/main/kotlin/io/schinzel/sample/pages/simple_page/Page.kt")
    }

    @Test
    fun webBlocksRoutesEndpoint_showsRouteParameters() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val html = connection.inputStream.bufferedReader().use { it.readText() }

        // Then
        // Should show parameters for routes that have them
        assertThat(html).contains("Parameter")
        assertThat(html).contains("user-id")

        // Should show page-block-api routes
        assertThat(html).contains("Block APIs:")
        assertThat(html).contains("/page-block-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name")
    }

    @Test
    fun webBlocksRoutesEndpoint_contentTypeIsHtml() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val contentType = connection.contentType

        // Then
        assertThat(contentType).contains("text/html")
    }

    @Test
    fun webBlocksRoutesEndpoint_showsPageAndApiSections() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val html = connection.inputStream.bufferedReader().use { it.readText() }

        // Then
        val pagesSection =
            html
                .substringAfter("Pages &amp; Components")
                .substringBefore("API Routes")
        val apiSection =
            html
                .substringAfter("API Routes")
                .substringBefore("</body>")

        // Pages section should contain page routes
        assertThat(pagesSection).contains("Page")

        // API section should contain API routes
        assertThat(apiSection).contains("UserPets")
    }
}
