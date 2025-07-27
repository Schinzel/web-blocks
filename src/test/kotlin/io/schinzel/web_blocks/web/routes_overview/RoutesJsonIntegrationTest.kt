package io.schinzel.web_blocks.web.routes_overview

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
 * The purpose of this class is to test the routes JSON endpoint integration
 * using the sample app to ensure it works in a real environment.
 *
 * Written by Claude Sonnet 4
 */
class RoutesJsonIntegrationTest {
    private lateinit var javalin: Javalin
    private val testPort = 7889
    private val baseUrl = "http://localhost:$testPort"
    private val objectMapper = jacksonObjectMapper()

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
    fun routesJsonEndpoint_returnsValidJson() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes-json").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val responseCode = connection.responseCode

        // Then
        assertThat(responseCode).isEqualTo(200)
    }

    @Test
    fun routesJsonEndpoint_containsExpectedStructure() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes-json").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
        val jsonMap: Map<String, Any> = objectMapper.readValue(jsonString)

        // Then
        assertThat(jsonMap).containsKey("pages")
        assertThat(jsonMap).containsKey("apis")
        assertThat(jsonMap["pages"]).isInstanceOf(List::class.java)
        assertThat(jsonMap["apis"]).isInstanceOf(List::class.java)
    }

    @Test
    fun routesJsonEndpoint_parsesAsValidJson() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes-json").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When/Then - Should not throw exception
        val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
        val jsonMap: Map<String, Any> = objectMapper.readValue(jsonString)

        // Verify basic structure exists
        assertThat(jsonMap).isNotNull()
    }

    @Test
    fun routesJsonEndpoint_containsSampleAppRoutes() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes-json").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
        val jsonMap: Map<String, Any> = objectMapper.readValue(jsonString)

        // Then
        val apis = jsonMap["apis"] as List<Map<String, Any>>
        val pages = jsonMap["pages"] as List<Map<String, Any>>

        // Should contain UserPets API
        val userPetsApi = apis.find { it["path"] == "/api/user-pets" }
        assertThat(userPetsApi).isNotNull
        assertThat(userPetsApi!!["className"]).isEqualTo("io.schinzel.sample.api.UserPets")
        assertThat(userPetsApi["simpleClassName"]).isEqualTo("UserPets")

        // Should contain simple page
        val simplePage = pages.find { it["path"] == "/simple-page" }
        assertThat(simplePage).isNotNull
        assertThat(simplePage!!["className"]).isEqualTo("io.schinzel.sample.pages.simple_page.Page")
        assertThat(simplePage["simpleClassName"]).isEqualTo("Page")
    }

    @Test
    fun routesJsonEndpoint_containsParameterInformation() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes-json").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
        val jsonMap: Map<String, Any> = objectMapper.readValue(jsonString)

        // Then
        val pages = jsonMap["pages"] as List<Map<String, Any>>

        // Find page with parameters
        val pageWithParams =
            pages.find { page ->
                val params = page["parameters"] as List<Map<String, Any>>
                params.isNotEmpty()
            }

        if (pageWithParams != null) {
            val parameters = pageWithParams["parameters"] as List<Map<String, Any>>
            assertThat(parameters).isNotEmpty()

            // Each parameter should have name and type
            parameters.forEach { param ->
                assertThat(param).containsKey("name")
                assertThat(param).containsKey("type")
                assertThat(param["name"]).isInstanceOf(String::class.java)
                assertThat(param["type"]).isInstanceOf(String::class.java)
            }
        }
    }

    @Test
    fun routesJsonEndpoint_containsBlockHierarchy() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes-json").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
        val jsonMap: Map<String, Any> = objectMapper.readValue(jsonString)

        // Then
        val pages = jsonMap["pages"] as List<Map<String, Any>>

        // Find page with blocks
        val pageWithBlocks =
            pages.find { page ->
                val blocks = page["blocks"] as List<Map<String, Any>>
                blocks.isNotEmpty()
            }

        if (pageWithBlocks != null) {
            val blocks = pageWithBlocks["blocks"] as List<Map<String, Any>>
            assertThat(blocks).isNotEmpty()

            // Each block should have required fields
            blocks.forEach { block ->
                assertThat(block).containsKey("name")
                assertThat(block).containsKey("path")
                assertThat(block).containsKey("className")
                assertThat(block).containsKey("simpleClassName")
                assertThat(block).containsKey("parameters")
            }
        }

        // Find page with block APIs
        val pageWithBlockApis =
            pages.find { page ->
                val blockApis = page["blockApis"] as List<Map<String, Any>>
                blockApis.isNotEmpty()
            }

        if (pageWithBlockApis != null) {
            val blockApis = pageWithBlockApis["blockApis"] as List<Map<String, Any>>
            assertThat(blockApis).isNotEmpty()

            // Each block API should have required fields
            blockApis.forEach { blockApi ->
                assertThat(blockApi).containsKey("name")
                assertThat(blockApi).containsKey("path")
                assertThat(blockApi).containsKey("className")
                assertThat(blockApi).containsKey("simpleClassName")
                assertThat(blockApi).containsKey("parameters")
            }
        }
    }

    @Test
    fun routesJsonEndpoint_contentTypeIsJson() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes-json").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val contentType = connection.contentType

        // Then
        assertThat(contentType).contains("application/json")
    }

    @Test
    fun routesJsonEndpoint_routesAreGroupedCorrectly() {
        // Given
        val url = URI("$baseUrl/web-blocks/routes-json").toURL()
        val connection = url.openConnection() as HttpURLConnection

        // When
        val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
        val jsonMap: Map<String, Any> = objectMapper.readValue(jsonString)

        // Then
        val apis = jsonMap["apis"] as List<Map<String, Any>>
        val pages = jsonMap["pages"] as List<Map<String, Any>>

        // All API routes should have /api/ prefix
        apis.forEach { api ->
            val path = api["path"] as String
            assertThat(path).startsWith("/api/")
        }

        // Pages should not have /api/ prefix
        pages.forEach { page ->
            val path = page["path"] as String
            assertThat(path).doesNotStartWith("/api/")
        }
    }
}
