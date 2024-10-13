package io.schinzel.my_package.db

import com.mongodb.client.MongoCollection
import org.bson.conversions.Bson
import org.litote.kmongo.*
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class AbstractDao<T : IDbElement>(private val collection_name: String) {
    protected val collection: MongoCollection<T> by lazy {
        val entity_class: KClass<T> = getDaoEntityClass()
        Database.database
                .getCollection(collection_name, entity_class.java)
    }


    fun delete(id: String) {
        collection.deleteOneById(id)
    }


    fun getById(id: String): T = collection
            .findOneById(id)
            ?: throw Exception("No document with id '$id' exists in collection $collection_name")

    fun exists(id: String): Boolean {
        val bson: Bson = AbstractDatabaseElement::_id eq id
        return exists(bson)
    }

    fun exists(bson: Bson): Boolean{
        val number_of_documents: Long = this.collection
                .countDocuments(bson)
        return number_of_documents > 0
    }


    fun update(element: T) {
        collection.replaceOneById(element._id, element)
    }


    //------------------------------------------------------------------------
    // PROTECTED
    //------------------------------------------------------------------------

    protected fun getAll(): List<T> = collection
            .find()
            .toList()


    protected fun add(element: T): T {
        collection.insertOne(element)
        return element
    }


    //------------------------------------------------------------------------
    // PRIVATE
    //------------------------------------------------------------------------

    @Suppress("UNCHECKED_CAST")
    private fun getDaoEntityClass(): KClass<T> =
            ((this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>).kotlin
}