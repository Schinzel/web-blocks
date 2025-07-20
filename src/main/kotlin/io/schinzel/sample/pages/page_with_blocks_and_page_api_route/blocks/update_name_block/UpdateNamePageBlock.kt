package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameDao
import io.schinzel.web_blocks.component.page.WebBlock
import io.schinzel.web_blocks.component.template_engine.TemplateProcessor
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.annotations.PageBlock

@PageBlock
class UpdateNamePageBlock(
    val userId: Int,
) : WebBlock() {
    private val firstName = NameDao(userId).getFirstName()

    override suspend fun getResponse(): IHtmlResponse =
        html(
            TemplateProcessor(this)
                .addData("firstName", firstName)
                .addData("userId", userId)
                .processTemplate("template.html"),
        )
}
