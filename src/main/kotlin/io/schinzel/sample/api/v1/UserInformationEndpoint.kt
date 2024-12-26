package io.schinzel.sample.api.v1

import io.schinzel.web_app_engine.route_registry.processors.IApiEndpoint

@Suppress("unused")
class UserInformationEndpoint(private val userId: String = "") : IApiEndpoint {
    override fun getResponse(): Any {
        if (userId.isNotEmpty()) {
            return UserInformation(userId, "Jane Doe", 24)
        }
        return UserInformation("", "", 0)
    }

    data class UserInformation(val userId: String, val name: String, val age: Int)
}

