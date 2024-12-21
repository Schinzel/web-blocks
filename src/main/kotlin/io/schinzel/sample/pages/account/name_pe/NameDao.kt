package io.schinzel.sample.pages.account.name_pe

class NameDao(private val userId: Int) {
    fun getFirstName(): String {
        return "John"
    }

    fun getLastName(): String {
        return "Doe"
    }
}