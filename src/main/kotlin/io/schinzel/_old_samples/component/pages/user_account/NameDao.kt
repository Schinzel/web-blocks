package io.schinzel._old_samples.component.pages.user_account

class NameDao(private val userId: Int) {

    fun getFirstName(): String {
        return mockDataStorage[userId] ?: "John"
    }

    fun setFirstName(firstName: String) {
        mockDataStorage[userId] = firstName

    }
}

private val mockDataStorage = mutableMapOf<Int, String>()


