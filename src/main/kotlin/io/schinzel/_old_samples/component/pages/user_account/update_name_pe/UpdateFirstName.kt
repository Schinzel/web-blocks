package io.schinzel._old_samples.component.pages.user_account.update_name_pe

import io.schinzel._old_samples.component.pages.user_account.NameDao
import io.schinzel.page_elements.web.routes.IPageApiRoute

@Suppress("unused")
class UpdateFirstName(
    val userId: Int, val firstName: String
) : IPageApiRoute {
    override fun getResponse(): Any {
        NameDao(userId).setFirstName(firstName)
        return "First name updated"
    }
}