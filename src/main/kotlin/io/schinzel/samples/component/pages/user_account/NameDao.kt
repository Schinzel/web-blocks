package io.schinzel.samples.component.pages.user_account

class NameDao(private val userId: Int) {

    fun getFirstName(): String {
        return userIdToNameMap[userId] ?: "John"
    }

    fun setFirstName(firstName: String) {
        userIdToNameMap[userId] = firstName

    }
}

private val userIdToNameMap = mutableMapOf<Int, String>()


