package io.schinzel.samples.component.pages.user_account.update_name_pe

import io.schinzel.page_elements.web.response_handlers.IPageEndpointResponseHandler
import io.schinzel.samples.component.pages.user_account.NameDao

@Suppress("unused")
class UpdateFirstName(
    val userId: Int, val firstName: String
) : IPageEndpointResponseHandler {
    override fun getResponse(): Any {
        NameDao(userId).setFirstName(firstName)
        return "First name updated"
    }
}