package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameDao
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.json
import io.schinzel.web_blocks.web.routes.IPageApiRoute

/**
 * The purpose of this class is to save a updated name to database
 */
@Suppress("unused")
class UpdateFirstNameRoute(
    val userId: Int,
    val firstName: String,
) : IPageApiRoute {
    override suspend fun getResponse(): WebBlockResponse {
        NameDao(userId).setFirstName(firstName)
        return json("First name updated")
    }
}
