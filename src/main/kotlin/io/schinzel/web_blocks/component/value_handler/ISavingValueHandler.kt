package io.schinzel.web_blocks.component.value_handler

/**
 * The purpose of this interface is to be a shorthand for the most common
 * value handler situation, persisting data sent from the client. Persisting
 * data sent from the client - such as persisting a first name of a person -
 * typically has two steps:
 * 1 - validate the data being persisted
 * 2 - persist the data
 */
interface ISavingValueHandler<T> : IValueHandler<T> {
    override suspend fun handle(data: T): IValueHandlerResponse {
        // Validate the data to persist
        val validationResponse = validate(data)
        // If the validation failed, return the validation response
        if (validationResponse.failed) return validationResponse
        // Persist and return the persist response
        return persist(data)
    }

    /**
     * If the validation fails:
     * 1) The [ValueHandlerResponse.status] should be [ValueHandlerStatus.VALIDATION_ERROR]
     * 2) It is recommended that [ValueHandlerResponse.errorMessages] contains all
     * validation errors and not just the first so that
     * a) The user get full information of what fails
     * b) and each validation error is a separate element in the list as to
     * facilitate a better presentation client side.
     */
    suspend fun validate(data: T): IValueHandlerResponse
    suspend fun persist(data: T): IValueHandlerResponse
}
