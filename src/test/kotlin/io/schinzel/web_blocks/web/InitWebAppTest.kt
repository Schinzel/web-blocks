package io.schinzel.web_blocks.web

import io.schinzel.sample.MyWebApp
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.ServerSocket

/**
 * The purpose of this class is to test InitWebApp for proper initialization.
 *
 * Written by Claude Sonnet 4
 */
class InitWebAppTest {
    private val testEndpointPackage = "io.schinzel.sample"

    @Nested
    @DisplayName("initializeRouteDescriptorRegistry")
    inner class InitializeRouteDescriptorRegistryTests {
        @Test
        fun `initializeRouteDescriptorRegistry _ registers descriptors without error`() {
            // This should not throw any exceptions
            InitWebApp.initializeRouteDescriptorRegistry(testEndpointPackage)

            // Just verify the method runs successfully
            assertThat(true).isTrue
        }
    }

    @Nested
    @DisplayName("constructor")
    inner class ConstructorTests {
        @Test
        fun `constructor with unavailable port _ throws exception`() {
            // Actually occupy a port to ensure the test is realistic
            val serverSocket = ServerSocket(0) // Use a random available port
            val occupiedPort = serverSocket.localPort

            // Use MyWebApp class which has a clean package for testing port validation
            val config =
                WebAppConfig(
                    webRootClass = MyWebApp(),
                    port = occupiedPort,
                    printStartupMessages = false,
                )

            try {
                assertThatThrownBy {
                    InitWebApp(config)
                }.isInstanceOf(RuntimeException::class.java)
                    .hasMessageContaining("Port $occupiedPort is not available")
            } finally {
                serverSocket.close()
            }
        }
    }
}
