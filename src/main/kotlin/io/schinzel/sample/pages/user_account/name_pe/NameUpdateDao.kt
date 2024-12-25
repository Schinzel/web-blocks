package io.schinzel.sample.pages.user_account.name_pe

import io.schinzel.web_app_engine.IWebPageEndpoint

@Suppress("unused")
class NameUpdateDao(
    userId: String,
    firstName: String,
    lastName: String
) : IWebPageEndpoint {
    init {
        println("Saving to db: userId: $userId, firstName: $firstName, lastName: $lastName")
    }


    override fun getResponse(): Any {
        return "Name successfully updated"
    }
}