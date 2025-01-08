package io.schinzel.page_elements.web

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class WebAppConfigTest {

    @Test
    fun `Existing package - no error`() {
        assertDoesNotThrow { WebAppConfig(this) }
    }

    @Test
    fun `Non existing package - throw error`() {
        val endpointPackage = "io.schinzel.page_elements.non_existing_package"
        assertThrows<Exception> {
            WebAppConfig(endpointPackage)
        }
    }

    @Test
    fun `Valid port - no error`() {
        assertDoesNotThrow { WebAppConfig(this, port = 5555) }
    }

    @Test
    fun `Not valid port - throw error`() {
        assertThrows<Exception> {
            WebAppConfig(this, port = 0)
        }
    }

    @Test
    fun `Valid timezone - no error`() {
        assertDoesNotThrow { WebAppConfig(this, localTimezone = "Europe/Stockholm") }
    }

    @Test
    fun `Not valid timezone - throw error`() {
        assertThrows<Exception> {
            WebAppConfig(this, localTimezone = "Europe/non_existing_timezone")
        }
    }

}