package io.schinzel.sample.pages.user_account.name_pe

import io.schinzel.page_elements.web_response.IPageElement
import io.schinzel.page_elements.file_util.TemplateProcessor

class NamePe(userId: Int): IPageElement {
    private val nameReadDao = NameReadDao(userId)
    override fun getHtml(): String {
        return TemplateProcessor("name_pe.html", this)
            .addData("firstName", nameReadDao.getFirstName())
            .addData("lastName", nameReadDao.getLastName())
            .getProcessedTemplate()
    }
}