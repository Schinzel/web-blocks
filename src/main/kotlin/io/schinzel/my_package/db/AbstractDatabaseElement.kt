package io.schinzel.my_package.db

import java.io.Serializable
import java.time.Instant

abstract class AbstractDatabaseElement(
        override val _id: String = GUID.generate(),
        override val created: Instant = Instant.now(),
) : IDbElement, Serializable