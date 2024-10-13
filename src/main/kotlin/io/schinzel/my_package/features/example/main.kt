package io.schinzel.my_package.features.example

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.basicutils.RandomUtil

fun main() {
    "Starting...".println()

    PeopleDao().createPerson(
            firstName = "Henrik",
            lastName = "Svensson_" + RandomUtil.getRandomString(4),
            RandomUtil.getRandomNumber(20, 77)
    )
}