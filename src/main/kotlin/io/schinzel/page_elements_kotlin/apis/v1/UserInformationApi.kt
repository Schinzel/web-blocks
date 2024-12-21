package io.schinzel.page_elements_kotlin.apis.v1

import io.schinzel.page_elements_kotlin.stuff.IApi

@Suppress("unused")
class UserInformationApi: IApi {
    override fun getData(): Any {
        return UserInformation("John Doe", 42)
    }
}

data class UserInformation(val name: String, val age: Int)