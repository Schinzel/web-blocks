package io.schinzel.web_blocks.component.value_handler

import io.schinzel.web_blocks.web.response.HtmlContentResponse

/**
 * The purpose of this interface is to be a shorthand for the most common
 * value handler situation, saving data sent from the client. Saving
 * data sent from the client - such as saving a first name of a person -
 * typically has two steps:
 * 1 - validate the data being saved
 * 2 - save the data
 */
interface ISavingValueHandler : IValueHandler {
    override suspend fun handle(value: String, parameters: Map<String, String>): HtmlContentResponse {
        // Validate the data to save
        val validationResponse = validate(value)
        // If the validation failed, return the validation response
        if (validationResponse.status != 200) return validationResponse
        // Save and return the save response
        return save(value, parameters)
    }

    suspend fun validate(data: String): HtmlContentResponse
    suspend fun save(data: String, parameters: Map<String, String>): HtmlContentResponse
}


