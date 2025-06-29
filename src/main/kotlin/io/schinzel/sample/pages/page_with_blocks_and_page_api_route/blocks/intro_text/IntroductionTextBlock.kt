package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.intro_text

import io.schinzel.web_blocks.component.page.ObservableBlock
import io.schinzel.web_blocks.component.template_engine.TemplateProcessor
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameDao

class IntroductionTextBlock(val userId: Int) : ObservableBlock() {

    private val firstName = NameDao(userId).getFirstName()
    override suspend fun getResponse(): String {
        return TemplateProcessor(this)
            .addData("firstName", firstName)
            .processTemplate("template.html")
    }
}