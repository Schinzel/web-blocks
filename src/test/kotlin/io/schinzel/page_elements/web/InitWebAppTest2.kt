package io.schinzel.page_elements.web

import io.schinzel.page_elements.web.test_routes2.MyWebApp2
import io.schinzel.page_elements.web.test_routes3.MyWebApp3
import io.schinzel.page_elements.web.test_routes4.MyWebApp4
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InitWebAppTest2 {

    @Nested
    inner class Start {
        
        @Test
        fun `page named api _ throws error`() {
            assertThrows<Exception> {
                MyWebApp2().start()
            }
        }

        @Test
        fun `page named static _ throws error`() {
            assertThrows<Exception> {
                MyWebApp3().start()
            }
        }

        @Test
        fun `page named page-api _ throws error`() {
            assertThrows<Exception> {
                MyWebApp4().start()
            }
        }
    }
}