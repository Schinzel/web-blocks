package io.schinzel.sample2.pages.user_account.update_name_pe

import io.schinzel.sample2.pages.user_account.NameDao
import io.schinzel.web_app_engine.response_handlers.response_handlers.IPageEndpointResponseHandler

class UpdateFirstName(
    val userId: Int, val firstName: String
) : IPageEndpointResponseHandler {
    override fun getResponse(): Any {
        NameDao(userId).setFirstName(firstName)
        return "First name updated"
    }
}