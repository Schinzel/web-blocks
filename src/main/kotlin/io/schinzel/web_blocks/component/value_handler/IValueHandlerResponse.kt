package io.schinzel.web_blocks.component.value_handler


/**
 * The purpose of this interface is to represent a response from a value handler
 */
interface IValueHandlerResponse {
    val status: ValueHandlerStatus
    val errorMessages: List<String>

    val failed: Boolean get() = status != ValueHandlerStatus.SUCCESS
}

data class ValueHandlerResponse(
    override val status: ValueHandlerStatus,
    override val errorMessages: List<String> = emptyList()
) : IValueHandlerResponse

/**
 * The purpose of this enum is to represent that state of a how
 * a value handler handle-operation went
 */
enum class ValueHandlerStatus { SUCCESS, VALIDATION_ERROR, SERVER_ERROR }
