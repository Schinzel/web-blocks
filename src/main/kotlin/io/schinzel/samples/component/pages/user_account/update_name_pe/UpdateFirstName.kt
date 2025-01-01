package io.schinzel.samples.component.pages.user_account.update_name_pe

import io.schinzel.samples.component.pages.user_account.NameDao
import io.schinzel.web.response_handlers.response_handlers.IPageEndpointResponseHandler

@Suppress("unused")
class UpdateFirstName(
    val userId: Int, val firstName: String
) : IPageEndpointResponseHandler {
    override fun getResponse(): Any {
        NameDao(userId).setFirstName(firstName)
        return "First name updated"
    }
}