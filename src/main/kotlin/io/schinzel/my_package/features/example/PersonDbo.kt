package io.schinzel.my_package.features.example

import io.schinzel.my_package.db.AbstractDatabaseElement

class PersonDbo(
        val firstName: String,
        val lastName: String,
        val age: Int
) : AbstractDatabaseElement()