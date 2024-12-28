package io.schinzel.sample2.pages.user_account.update_name_pe

import io.schinzel.pages.template_engine.TemplateRenderer
import io.schinzel.sample2.pages.user_account.name_pe.IPageElement
import io.schinzel.sample2.pages.user_account.name_pe.NameReadDao

class UpdateNamePe(private val userId: Int) : IPageElement {
    private val firstName = NameReadDao(userId).getFirstName()

    override fun getHtml(): String {
        return TemplateRenderer("template.html", this)
            .addData("firstName", firstName)
            .process()
    }
}