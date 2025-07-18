package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameDao
import io.schinzel.web_blocks.component.page.ObservableBlock
import io.schinzel.web_blocks.component.template_engine.TemplateProcessor
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html

class UpdateNameBlock(
    val userId: Int,
) : ObservableBlock() {
    private val firstName = NameDao(userId).getFirstName()

    override suspend fun getResponse(): WebBlockResponse =
        html(
            TemplateProcessor(this)
                .addData("firstName", firstName)
                .addData("userId", userId)
                .processTemplate("template.html"),
        )
}
