package io.schinzel.sample2.pages.user_account.name_pe

class NameReadDao(private val userId: Int) {
    fun getFirstName(): String {
        return "John"
    }
}