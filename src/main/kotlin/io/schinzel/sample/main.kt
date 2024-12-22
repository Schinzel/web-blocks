package io.schinzel.sample

import io.schinzel.page_elements.InitWeb

fun main() {
    val pagePackage = "io.schinzel.sample.pages"
    val apiPackage = "io.schinzel.sample.apis"
    InitWeb(pagePackage, apiPackage)
}
