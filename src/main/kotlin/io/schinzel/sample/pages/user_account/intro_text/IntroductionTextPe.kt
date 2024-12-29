package io.schinzel.sample.pages.user_account.intro_text

import io.schinzel.pages.bootstrap_page.ObservablePageElement
import io.schinzel.pages.template_engine.TemplateRenderer
import io.schinzel.sample.pages.user_account.NameDao

class IntroductionTextPe(val userId: Int) : ObservablePageElement() {

    private val firstName = NameDao(userId).getFirstName()
    override fun getResponse(): String {
        return TemplateRenderer("template.html", this)
            .addData("firstName", firstName)
            .process()
    }
}