package io.schinzel.samples.component.pages.user_account.update_name_pe

import io.schinzel.components.bootstrap_page.ObservablePageElement
import io.schinzel.components.template_engine.TemplateRenderer
import io.schinzel.samples.component.pages.user_account.NameDao

class UpdateNamePe(val userId: Int) : ObservablePageElement() {
    private val firstName = NameDao(userId).getFirstName()

    override fun getResponse(): String {
        return TemplateRenderer("template.html", this)
            .addData("firstName", firstName)
            .addData("userId", userId)
            .process()
    }
}