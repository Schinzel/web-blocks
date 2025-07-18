package io.schinzel.web_blocks.web

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class WebAppConfigTest {
    @Nested
    inner class Constructor {
        @Test
        fun `existing package _ no error`() {
            assertDoesNotThrow { WebAppConfig(this@WebAppConfigTest) }
        }

        @Test
        fun `non existing package _ throws error`() {
            val endpointPackage = "io.schinzel.web_blocks.non_existing_package"
            assertThrows<Exception> {
                WebAppConfig(endpointPackage)
            }
        }

        @Test
        fun `valid port _ no error`() {
            assertDoesNotThrow { WebAppConfig(this@WebAppConfigTest, port = 5555) }
        }

        @Test
        fun `invalid port _ throws error`() {
            assertThrows<Exception> {
                WebAppConfig(this@WebAppConfigTest, port = 0)
            }
        }

        @Test
        fun `valid timezone _ no error`() {
            assertDoesNotThrow { WebAppConfig(this@WebAppConfigTest, localTimezone = "Europe/Stockholm") }
        }

        @Test
        fun `invalid timezone _ throws error`() {
            assertThrows<Exception> {
                WebAppConfig(this@WebAppConfigTest, localTimezone = "Europe/non_existing_timezone")
            }
        }
    }
}
