package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameDb

class NameWriteDao(
    private val userId: Int,
) {
    fun setFirstName(firstName: String) {
        NameDb.userIdToNameMap[userId] = firstName
    }
}
