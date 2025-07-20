package io.schinzel.web_blocks.web.routes.annotations

/**
 * The purpose of this annotation is to mark a class as a WebBlock component
 * that returns HTML content for a reusable UI component.
 *
 * WebBlocks are standalone, independent HTML components that can be composed
 * into pages. Each block represents a self-contained piece of functionality
 * with its own HTML template, styling, and behavior.
 *
 * Key characteristics:
 * - Returns HTML content (IHtmlResponse)
 * - Can be embedded in pages or other blocks
 * - Self-contained with own resources (HTML, CSS, JS)
 * - Supports real-time updates without page refresh
 * - Follows observer pattern for inter-block communication
 *
 * Path generation follows file system structure:
 * - /pages/profile/blocks/avatar_block/AvatarBlock.kt → /web-block/profile/blocks/avatar-block/avatar-block
 * - /pages/dashboard/widgets/weather/WeatherWidget.kt → /web-block/dashboard/widgets/weather/weather-widget
 * - PascalCase class names become kebab-case, "Block" suffix removed if present
 *
 * Example - Avatar display block:
 * @WebBlock
 * class AvatarBlock : WebBlock() {
 *     override suspend fun getResponse(): WebBlockResponse = html(renderTemplate())
 * }
 *
 * This allows the avatar component to be reused across multiple pages
 * and update independently when user data changes.
 *
 * Written by Claude Sonnet 4
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Suppress("unused")
annotation class WebBlock
