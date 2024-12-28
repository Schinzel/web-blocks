package io.schinzel.pages.bootstrap_page

import io.schinzel.sample2.pages.user_account.name_pe.IPageElement
import io.schinzel.pages.template_engine.TemplateRenderer

class BootstrapPage {
    private var title = ""
    private val rows = mutableListOf<Row>()

    fun setTitle(title: String): BootstrapPage {
        this.title = title
        return this
    }

    fun addRow(): BootstrapPage {
        rows.add(Row())
        return this
    }

    fun addColumn(span: Int): BootstrapPage {
        rows.last().columns.add(Column(span))
        return this
    }

    fun addPageElement(pageElement: IPageElement): BootstrapPage {
        rows.last().columns.last().elements.add(pageElement)
        return this
    }

    fun getHtml(): String {
        val content = rows.joinToString("\n") { it.getHtml() }

        return TemplateRenderer("bootstrap-page-template.html", this)
            .addData("title", title)
            .addData("content", content)
            .process()
    }
}