package io.schinzel.sample.pages.user_account.name_pe

class NameReadDao(private val userId: Int) {
    fun getFirstName(): String {
        return "John"
    }

    fun getLastName(): String {
        return "Doe"
    }
}