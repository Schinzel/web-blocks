package io.schinzel.samples.component.pages.user_account.intro_text

import io.schinzel.page_elements.component.page.ObservablePageElement
import io.schinzel.page_elements.component.template_engine.TemplateProcessor
import io.schinzel.page_elements.samples.component.pages.user_account.NameDao

class IntroductionTextPe(val userId: Int) : ObservablePageElement() {

    private val firstName = NameDao(userId).getFirstName()
    override fun getResponse(): String {
        return TemplateProcessor(this)
            .addData("firstName", firstName)
            .processTemplate("template.html")
    }
}