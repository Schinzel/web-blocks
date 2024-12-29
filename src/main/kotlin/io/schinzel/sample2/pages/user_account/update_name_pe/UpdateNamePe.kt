package io.schinzel.sample2.pages.user_account.update_name_pe

import io.schinzel.pages.bootstrap_page_v2.ObservablePageElement
import io.schinzel.pages.template_engine.TemplateRenderer
import io.schinzel.sample2.pages.user_account.NameDao

class UpdateNamePe(val userId: Int) : ObservablePageElement() {
    private val firstName = NameDao(userId).getFirstName()

    override fun getResponse(): String {
        return TemplateRenderer("template.html", this)
            .addData("firstName", firstName)
            .addData("userId", userId)
            .process()
    }
}