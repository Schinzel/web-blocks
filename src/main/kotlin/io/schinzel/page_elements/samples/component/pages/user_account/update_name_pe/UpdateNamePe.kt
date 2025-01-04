package io.schinzel.page_elements.samples.component.pages.user_account.update_name_pe

import io.schinzel.page_elements.component.page.ObservablePageElement
import io.schinzel.page_elements.component.template_engine.TemplateProcessor2
import io.schinzel.page_elements.samples.component.pages.user_account.NameDao

class UpdateNamePe(val userId: Int) : ObservablePageElement() {
    private val firstName = NameDao(userId).getFirstName()

    override fun getResponse(): String {
        return TemplateProcessor2(this)
            .addData("firstName", firstName)
            .addData("userId", userId)
            .processTemplate("template.html")
    }
}