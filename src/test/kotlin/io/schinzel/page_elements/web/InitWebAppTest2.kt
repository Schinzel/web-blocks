package io.schinzel.page_elements.web

import io.schinzel.page_elements.web.request_handler.log.NoLogger
import io.schinzel.page_elements.web.test_routes2.DummyClass2
import io.schinzel.page_elements.web.test_routes3.DummyClass3
import io.schinzel.page_elements.web.test_routes4.DummyClass4
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InitWebAppTest2 {

    @Test
    fun `Page named api - throes error`() {
        val randomPort = (49152..65535).random()
        // Create an instance of the class so that its package is available
        DummyClass2()
        val webAppConfig = WebAppConfig(
            routesPackage = "io.schinzel.page_elements.web.test_routes2",
            port = randomPort,
            logger = NoLogger(),
            prettyFormatHtml = false
        )
        assertThrows<Exception> {
            InitWebApp(webAppConfig)
        }
    }

    @Test
    fun `Page named static - throws error`() {
        val randomPort = (49152..65535).random()
        // Create an instance of the class so that its package is available
        DummyClass3()
        val webAppConfig = WebAppConfig(
            routesPackage = "io.schinzel.page_elements.web.test_routes3",
            port = randomPort,
            logger = NoLogger(),
            prettyFormatHtml = false
        )
        assertThrows<Exception> {
            InitWebApp(webAppConfig)
        }
    }

    @Test
    fun `Page named page-api - throws error`() {
        val randomPort = (49152..65535).random()
        // Create an instance of the class so that its package is available
        DummyClass4()
        val webAppConfig = WebAppConfig(
            routesPackage = "io.schinzel.page_elements.web.test_routes4",
            port = randomPort,
            logger = NoLogger(),
            prettyFormatHtml = false
        )
        assertThrows<Exception> {
            InitWebApp(webAppConfig)
        }
    }
}