package io.schinzel.my_package.db

import java.time.Instant

interface IDbElement {
    val _id: String
    val created: Instant
}