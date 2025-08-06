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
interface ISavingValueHandler<TValue, TContext> : IValueHandler<TValue, TContext> {
    override suspend fun handle(value: TValue, context: TContext): NotificationResponse {
        // Validate the data to save
        val validationResponse = validate(value)
        // If the validation failed, return the validation response
        if (validationResponse.status != 200) return NotificationResponseEnum.WARNING
            .create("Something something went wrong!")
        // Save and return the save response
        return save(value, context)
    }

    suspend fun validate(data: TValue): HtmlContentResponse
    suspend fun save(data: TValue, context: TContext): NotificationResponse
}


