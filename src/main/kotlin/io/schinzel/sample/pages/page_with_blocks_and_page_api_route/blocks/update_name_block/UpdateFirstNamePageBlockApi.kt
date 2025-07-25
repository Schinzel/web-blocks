package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block.NameWriteDao
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.jsonSuccess
import io.schinzel.web_blocks.web.routes.IJsonRoute
import io.schinzel.web_blocks.web.routes.annotations.PageBlockApi

/**
 * The purpose of this class is to save a updated name to database
 */
@Suppress("unused")
@PageBlockApi
class UpdateFirstNamePageBlockApi(
    val userId: Int,
    val firstName: String,
) : IJsonRoute {
    override suspend fun getResponse(): IJsonResponse {
        NameWriteDao(userId).setFirstName(firstName)
        return jsonSuccess("First name updated")
    }
}
