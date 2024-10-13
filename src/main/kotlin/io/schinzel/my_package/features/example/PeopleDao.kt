package io.schinzel.my_package.features.example

import io.schinzel.basicutils.RandomUtil
import io.schinzel.my_package.db.AbstractDao


class PeopleDao : AbstractDao<PersonDbo>("people") {

    fun createPerson(
            firstName: String,
            lastName: String,
            age: Int,
    ) {
        val person = PersonDbo(firstName, lastName, age)
        this.add(person)

    }
}
