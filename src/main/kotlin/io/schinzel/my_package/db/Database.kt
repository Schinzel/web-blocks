package io.schinzel.my_package.db

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.litote.kmongo.util.KMongoConfiguration

object Database {
    private const val DB_URL: String = "mongodb://localhost:27017/"
    private const val DB_NAME: String = "my_database"
    val database: MongoDatabase by lazy {
        // Configuration for db storage
        KMongoConfiguration.bsonMapper
                // Also store private properties
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                // Do not store getter-functions
                .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
                // Do not store is-functions
                .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)

        KMongo
                .createClient(DB_URL)
                .getDatabase(DB_NAME)


    }


    fun deleteDB() {
        database.drop()
    }
}