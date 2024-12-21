package io.schinzel.my_package.web_server

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.schinzel.page_elements_kotlin.pages.account.Page
import io.schinzel.page_elements_kotlin.pages.landing.LandingPage
import se.refur.javalin.JavalinAnnotation
import se.refur.javalin.exposeClassEndpoints

class WebServer {
    init {
        // Setup roles for endpoints
        JavalinAnnotation.setRoles(MyAccessRoles.entries.associateBy { it.name })

        Javalin.create { config ->
            config.staticFiles.add("/site", Location.CLASSPATH)
            config.accessManager { handler, ctx, _ -> handler.handle(ctx) }
        }
            .get("/hello") { ctx -> ctx.result("Hello Public World") }
            .get("/") { ctx ->
                // Dynamically generate the HTML using your LandingPage logic
                val htmlContent = LandingPage().getHtml()
                // Respond with the generated HTML
                ctx.html(htmlContent)
            }
            .get("/account") { ctx ->
                // Dynamically generate the HTML using your LandingPage logic
                val htmlContent = Page()
                    .getHtml()
                // Respond with the generated HTML
                ctx.html(htmlContent)
            }
            .exposeClassEndpoints(MyApi::class)
            .start(5555)
            .get("/hello2") { ctx -> ctx.result("Hello Public World") }
    }
}

fun main() {
    WebServer()
}