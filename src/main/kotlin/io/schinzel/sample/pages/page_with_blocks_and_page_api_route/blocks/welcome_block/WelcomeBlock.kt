package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.welcome_block

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameDao
import io.schinzel.web_blocks.component.page_builder.WebBlock
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.annotations.PageBlock

@PageBlock
class WelcomeBlock(
    val userId: Int,
) : WebBlock() {
    private val firstName = NameDao(userId).getFirstName()

    override suspend fun getResponse(): IHtmlResponse = html("<h1>Welcome $firstName</h1>")
}
