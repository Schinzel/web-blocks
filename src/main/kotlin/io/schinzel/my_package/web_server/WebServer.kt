package io.schinzel.my_package.web_server

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
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
                .get("/") { ctx -> ctx.result("Hello Public World") }
                .exposeClassEndpoints(MyApi::class)
                .start(5555)
    }
}