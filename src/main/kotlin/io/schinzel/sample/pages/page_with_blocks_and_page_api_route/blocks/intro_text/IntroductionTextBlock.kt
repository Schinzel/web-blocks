package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.intro_text

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameDao
import io.schinzel.web_blocks.component.page.ObservableBlock
import io.schinzel.web_blocks.component.template_engine.TemplateProcessor
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi

@WebBlockPageApi
class IntroductionTextBlock(
    val userId: Int,
) : ObservableBlock() {
    private val firstName = NameDao(userId).getFirstName()

    override suspend fun getResponse(): WebBlockResponse =
        html(
            TemplateProcessor(this)
                .addData("firstName", firstName)
                .processTemplate("template.html"),
        )
}
