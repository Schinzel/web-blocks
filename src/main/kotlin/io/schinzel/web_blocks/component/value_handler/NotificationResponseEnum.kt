package io.schinzel.web_blocks.component.value_handler

/**
 * The purpose of this enum is to define the three types of notifications
 * that value handlers can return, with each type encapsulating its correct
 * HTTP status code and providing factory methods for type-safe creation.
 *
 * Written by Claude Sonnet 4
 */
enum class NotificationResponseEnum(
    val statusCode: Int,
) {
    SUCCESS(200),
    WARNING(422),
    ERROR(500),
    ;

    /**
     * Creates a NotificationResponse with this notification type
     * @param message The main notification message
     * @param details Optional list of detailed messages (e.g., validation errors)
     * @return NotificationResponse with correct status code for this type
     */
    fun create(
        message: String,
        details: List<String> = emptyList(),
    ): NotificationResponse = NotificationResponse(this, message, details, statusCode)
}
