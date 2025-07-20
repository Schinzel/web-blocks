package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.welcome_block

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameDao
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.annotations.WebBlock
import io.schinzel.web_blocks.component.page.WebBlock as WebBlockComponent

@WebBlock
class WelcomeBlock(
    val userId: Int,
) : WebBlockComponent() {
    private val firstName = NameDao(userId).getFirstName()

    override suspend fun getResponse(): IHtmlResponse = html("<h1>Welcome $firstName</h1>")
}
