package io.schinzel.sample.pages.page_with_page_route.page_elements.update_name_pe

import io.schinzel.page_elements.web.routes.IPageApiRoute
import io.schinzel.sample.pages.page_with_page_route.page_elements.NameDao

/**
 * The purpose of this class is to save a updated name to database
 */
@Suppress("unused")
class UpdateFirstNameRoute(
    val userId: Int, val firstName: String
) : IPageApiRoute {
    override fun getResponse(): Any {
        NameDao(userId).setFirstName(firstName)
        return "First name updated"
    }
}