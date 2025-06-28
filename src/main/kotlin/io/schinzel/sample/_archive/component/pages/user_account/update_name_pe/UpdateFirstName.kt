package io.schinzel.sample._archive.component.pages.user_account.update_name_pe

import io.schinzel.page_elements.web.routes.IPageApiRoute
import io.schinzel.sample._archive.component.pages.user_account.NameDao

@Suppress("unused")
class UpdateFirstName(
    val userId: Int, val firstName: String
) : IPageApiRoute {
    override fun getResponse(): Any {
        NameDao(userId).setFirstName(firstName)
        return "First name updated"
    }
}