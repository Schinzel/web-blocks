package io.schinzel.sample.pages.user_account.name_pe

import io.schinzel.page_elements.web_response.IEndpoint

class NameUpdateDao(
    userId: String,
    firstName: String,
    lastName: String
) : IEndpoint {
    init {
        println("Saving to db: userId: $userId, firstName: $firstName, lastName: $lastName")
    }


    override fun getResponse(): Any {
        return "Name successfully updated"
    }
}