package io.schinzel.my_package.web_server

import io.javalin.security.RouteRole

/**
 * The purpose of this enum is to declare the available roles
 * in the webserver
 */
enum class MyAccessRoles : RouteRole {
    PUBLIC, ADMIN
}
