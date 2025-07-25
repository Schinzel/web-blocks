package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks

/**
 * The purpose of this class is to read and write the first name from/to database
 */
class NameReadDao(
    private val userId: Int,
) {
    fun getFirstName(): String = NameDb.userIdToNameMap[userId] ?: "John"
}
