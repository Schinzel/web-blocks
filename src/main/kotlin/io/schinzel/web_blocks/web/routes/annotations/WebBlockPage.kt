package io.schinzel.web_blocks.web.routes.annotations

/**
 * The purpose of this annotation is to mark a class as a WebBlocks page route
 * that returns HTML content for web pages.
 *
 * Page routes are discovered from the /pages directory structure and generate
 * HTML responses with Content-Type: text/html.
 *
 * Path generation follows file system structure:
 * - /pages/simple_page/ThePage.kt → /simple-page
 * - /pages/landing/LandingPage.kt → / (root)
 * - snake_case directories become kebab-case URLs
 *
 * Example:
 * @WebBlockPage
 * class ThePage : WebBlockRoute {
 *     override suspend fun getResponse(): WebBlockResponse = html("<h1>Hello</h1>")
 * }
 *
 * Written by Claude Sonnet 4
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Suppress("unused")
annotation class WebBlockPage
