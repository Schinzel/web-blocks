package io.schinzel.sample.pages.account.name_pe

import io.schinzel.page_elements.IPageElement
import io.schinzel.page_elements.TemplateProcessor

class NamePe(userId: Int): IPageElement {
    private val nameDao = NameDao(userId)
    override fun getHtml(): String {
        return TemplateProcessor("name_pe.html", this)
            .addData("firstName", nameDao.getFirstName())
            .addData("lastName", nameDao.getLastName())
            .getProcessedTemplate()
    }
}