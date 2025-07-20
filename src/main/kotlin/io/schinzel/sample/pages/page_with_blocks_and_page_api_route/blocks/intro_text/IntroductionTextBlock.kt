package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.intro_text

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameDao
import io.schinzel.web_blocks.component.template_engine.TemplateProcessor
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.annotations.WebBlock
import io.schinzel.web_blocks.component.page.WebBlock as WebBlockComponent

@WebBlock
class IntroductionTextBlock(
    val userId: Int,
) : WebBlockComponent() {
    private val firstName = NameDao(userId).getFirstName()

    override suspend fun getResponse(): IHtmlResponse =
        html(
            TemplateProcessor(this)
                .addData("firstName", firstName)
                .processTemplate("template.html"),
        )
}
