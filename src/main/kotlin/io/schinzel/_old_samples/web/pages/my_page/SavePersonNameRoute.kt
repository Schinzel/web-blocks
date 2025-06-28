package io.schinzel._old_samples.web.pages.my_page

import io.schinzel.page_elements.web.routes.IPageApiRoute

@Suppress("unused")
class SavePersonNameRoute(
    val userId: String,
    val firstName: String
): IPageApiRoute {
    override fun getResponse(): Any {
        return "Set the first name to be '$firstName' for user '$userId'"
    }
}