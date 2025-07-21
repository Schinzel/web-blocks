package io.schinzel.web_blocks.web

import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorRegistry
import io.schinzel.web_blocks.web.test_routes2.MyWebApp2
import io.schinzel.web_blocks.web.test_routes3.MyWebApp3
import io.schinzel.web_blocks.web.test_routes4.MyWebApp4
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InitWebAppTest2 {
    @Nested
    inner class Start {
        @Test
        fun `page named api _ throws error`() {
            val app = MyWebApp2()
            try {
                assertThrows<Exception> {
                    app.start()
                }
            } finally {
                app.stop()
            }
        }

        @Test
        fun `page named static _ throws error`() {
            val app = MyWebApp3()
            try {
                assertThrows<Exception> {
                    app.start()
                }
            } finally {
                app.stop()
            }
        }

        @Test
        fun `page named page-block _ throws error`() {
            val app = MyWebApp4()
            try {
                assertThrows<Exception> {
                    app.start()
                }
            } finally {
                app.stop()
            }
        }
    }
}
