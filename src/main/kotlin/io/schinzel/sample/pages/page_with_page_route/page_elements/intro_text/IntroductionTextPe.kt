package io.schinzel.sample.pages.page_with_page_route.page_elements.intro_text

import io.schinzel.page_elements.component.page.ObservablePageElement
import io.schinzel.page_elements.component.template_engine.TemplateProcessor
import io.schinzel.sample.pages.page_with_page_route.page_elements.NameDao

class IntroductionTextPe(val userId: Int) : ObservablePageElement() {

    private val firstName = NameDao(userId).getFirstName()
    override fun getResponse(): String {
        return TemplateProcessor(this)
            .addData("firstName", firstName)
            .processTemplate("template.html")
    }
}