package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameReadDao
import io.schinzel.web_blocks.component.page_builder.WebBlock
import io.schinzel.web_blocks.component.template_engine.TemplateProcessor
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.annotations.PageBlock

@PageBlock
class UpdateNamePageBlock(
    val userId: Int,
) : WebBlock() {
    private val firstName = NameReadDao(userId).getFirstName()

    override suspend fun getResponse(): IHtmlResponse =
        html(
            TemplateProcessor(this)
                .withData("firstName", firstName)
                .withData("userId", userId)
                .processTemplate("template.html"),
        )
}
