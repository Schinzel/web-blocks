package io.schinzel.web_blocks.component.data_saver

import java.util.concurrent.ConcurrentHashMap

interface IDataSaver<T> {
    fun save(data: T): IDataSaverResponse
    fun validate(data: T): IDataSaverResponse
}

class FirstNameDataSaver : IDataSaver<String> {
    override fun save(data: String): DataSaverResponse {
        return DataSaverResponse(false, listOf("HARDCODED ERROR"))
    }

    override fun validate(data: String): IDataSaverResponse {
        return DataSaverResponse(false, listOf("HARDCODED ERROR"))
    }
}

interface IDataSaverResponse {
    val success: Boolean
    val errorMessages: List<String>
}


data class DataSaverResponse(
    override val success: Boolean,
    override val errorMessages: List<String> = emptyList()
) : IDataSaverResponse


class DataSaverRegistry {
    private val dataSavers = ConcurrentHashMap<String, IDataSaver<*>>()

    fun <T> registerDataSaver(dataSaverName: String, dataSaver: IDataSaver<T>) {
        val previous = dataSavers.putIfAbsent(dataSaverName, dataSaver)
        require(previous == null) { "'$dataSaverName' already registered" }
    }

    fun <T> registerDataSaver(
        name: String,
        saveFunc: (T) -> IDataSaverResponse,
        validateFunc: (T) -> IDataSaverResponse = { DataSaverResponse(true, emptyList()) }
    ) {
        val lambdaDataSaver = object : IDataSaver<T> {
            override fun save(data: T) = saveFunc(data)
            override fun validate(data: T) = validateFunc(data)
        }
        registerDataSaver(name, lambdaDataSaver)
    }


    fun <T> save(dataSaverName: String, data: T): IDataSaverResponse {
        return try {
            @Suppress("UNCHECKED_CAST")
            val dataSaver = dataSavers[dataSaverName] as? IDataSaver<T>
                ?: throw Exception("Data saver for name '$dataSaverName' is not registered.")

            val validationResponse = dataSaver.validate(data)
            if (!validationResponse.success) {
                return validationResponse
            }
            dataSaver.save(data)
        } catch (ex: Exception) {
            DataSaverResponse(
                false,
                listOf("Error when trying to save $data with data saver $dataSaverName: ${ex.message}")
            )
        }
    }

    companion object {
        @JvmStatic
        val instance = DataSaverRegistry() // Even simpler!
    }
}
