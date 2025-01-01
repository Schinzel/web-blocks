package io.schinzel.samples.component.pages.user_account.intro_text

import io.schinzel.component.bootstrap_page.ObservablePageElement
import io.schinzel.component.template_engine.TemplateRenderer
import io.schinzel.samples.component.pages.user_account.NameDao

class IntroductionTextPe(val userId: Int) : ObservablePageElement() {

    private val firstName = NameDao(userId).getFirstName()
    override fun getResponse(): String {
        return TemplateRenderer("template.html", this)
            .addData("firstName", firstName)
            .process()
    }
}