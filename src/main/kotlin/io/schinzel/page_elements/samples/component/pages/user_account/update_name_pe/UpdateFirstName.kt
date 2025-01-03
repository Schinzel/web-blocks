package io.schinzel.page_elements.samples.component.pages.user_account.update_name_pe

import io.schinzel.page_elements.samples.component.pages.user_account.NameDao
import io.schinzel.page_elements.web.response_handlers.IPageEndpointResponseHandler

@Suppress("unused")
class UpdateFirstName(
    val userId: Int, val firstName: String
) : IPageEndpointResponseHandler {
    override fun getResponse(): Any {
        NameDao(userId).setFirstName(firstName)
        return "First name updated"
    }
}