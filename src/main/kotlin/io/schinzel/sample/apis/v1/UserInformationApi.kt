package io.schinzel.sample.apis.v1

import io.schinzel.page_elements.IApi

@Suppress("unused")
class UserInformationApi(private val userId: String = "") : IApi {
    override fun getResponse(): Any {
        if (userId.isNotEmpty()) {
            return UserInformation(userId, "Jane Doe", 24)
        }
        return UserInformation("", "", 0)
    }

    data class UserInformation(val userId: String, val name: String, val age: Int)
}

