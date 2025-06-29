package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_pe

import io.schinzel.web_blocks.web.routes.IPageApiRoute
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameDao

/**
 * The purpose of this class is to save a updated name to database
 */
@Suppress("unused")
class UpdateFirstNameRoute(
    val userId: Int, val firstName: String
) : IPageApiRoute {
    override suspend fun getResponse(): Any {
        NameDao(userId).setFirstName(firstName)
        return "First name updated"
    }
}