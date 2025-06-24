package io.schinzel.samples.new_sample.pages.page_with_page_endpoint.page_elements

/**
 * The purpose of this class is to read and write the first name from/to database
 */
class NameDao(private val userId: Int) {

    fun getFirstName(): String {
        return userIdToNameMap[userId] ?: "John"
    }

    fun setFirstName(firstName: String) {
        userIdToNameMap[userId] = firstName

    }
}

private val userIdToNameMap = mutableMapOf<Int, String>()
