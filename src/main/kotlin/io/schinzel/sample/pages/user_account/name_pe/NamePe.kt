package io.schinzel.sample.pages.user_account.name_pe

import io.schinzel.pages.IPageElement
import io.schinzel.web_app_engine.template_engine.TemplateRenderer

class NamePe(userId: String): IPageElement {
    private val nameReadDao = NameReadDao(userId)
    override fun getHtml(): String {
        return TemplateRenderer("name_pe.html", this)
            .addData("firstName", nameReadDao.getFirstName())
            .addData("lastName", nameReadDao.getLastName())
            .process()
    }
}