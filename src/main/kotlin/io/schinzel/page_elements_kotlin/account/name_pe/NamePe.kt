package io.schinzel.page_elements_kotlin.account.name_pe

import io.schinzel.stuff.IPageElement
import io.schinzel.stuff.TemplateProcessor

class NamePe(userId: Int): IPageElement {
    private val nameDao = NameDao(userId)
    override fun getHtml(): String {
        return TemplateProcessor("name_pe.html", this)
            .addData("firstName", nameDao.getFirstName())
            .addData("lastName", nameDao.getLastName())
            .getProcessedTemplate()
    }
}