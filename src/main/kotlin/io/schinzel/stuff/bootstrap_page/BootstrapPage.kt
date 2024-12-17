package io.schinzel.stuff.bootstrap_page

import io.schinzel.stuff.IPage
import io.schinzel.stuff.IPageElement
import io.schinzel.stuff.TemplateProcessor

class BootstrapPage: IPage {
    private var title = ""
    val rows = mutableListOf<Row>()

    fun addRow(): BootstrapPage {
        rows.add(Row())
        return this
    }

    fun addColumn(span: Int): BootstrapPage {
        rows.last().columns.add(Column(span))
        return this
    }

    fun setTitle(title: String): BootstrapPage {
        this.title = title
        return this
    }

    override fun addPageElement(pageElement: IPageElement): BootstrapPage {
        rows.last().columns.last().elements.add(pageElement)
        return this
    }

    override fun getHtml(): String {
        val content = rows.joinToString("\n") { it.getHtml() }

        return TemplateProcessor("template.html", this)
            .addData("title", title)
            .addData("content", content)
            .getProcessedTemplate()
    }
}