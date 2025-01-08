package io.schinzel.page_elements.web

import io.schinzel.page_elements.web.test_routes2.MyWebApp2
import io.schinzel.page_elements.web.test_routes3.MyWebApp3
import io.schinzel.page_elements.web.test_routes4.MyWebApp4
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InitWebAppTest2 {

    @Test
    fun `Page named api - throes error`() {
        assertThrows<Exception> {
            MyWebApp2().start()
        }
    }

    @Test
    fun `Page named static - throws error`() {
        assertThrows<Exception> {
            MyWebApp3().start()
        }
    }

    @Test
    fun `Page named page-api - throws error`() {
        assertThrows<Exception> {
            MyWebApp4().start()
        }
    }
}