package io.schinzel.sample.pages.user_account.name_pe

import io.schinzel.page_elements.web_response.IPageElement
import io.schinzel.page_elements.file_util.TemplateRenderer

class NamePe(userId: Int): IPageElement {
    private val nameReadDao = NameReadDao(userId)
    override fun getHtml(): String {
        return TemplateRenderer("name_pe.html", this)
            .addData("firstName", nameReadDao.getFirstName())
            .addData("lastName", nameReadDao.getLastName())
            .process()
    }
}