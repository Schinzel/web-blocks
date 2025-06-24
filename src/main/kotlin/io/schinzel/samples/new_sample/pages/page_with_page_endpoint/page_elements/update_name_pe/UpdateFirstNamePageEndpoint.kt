package io.schinzel.samples.new_sample.pages.page_with_page_endpoint.page_elements.update_name_pe

import io.schinzel.page_elements.web.response_handlers.IPageEndpointResponseHandler
import io.schinzel.samples.new_sample.pages.page_with_page_endpoint.page_elements.NameDao

/**
 * The purpose of this class is to save a updated name to database
 */
@Suppress("unused")
class UpdateFirstNamePageEndpoint(
    val userId: Int, val firstName: String
) : IPageEndpointResponseHandler {
    override fun getResponse(): Any {
        NameDao(userId).setFirstName(firstName)
        return "First name updated"
    }
}