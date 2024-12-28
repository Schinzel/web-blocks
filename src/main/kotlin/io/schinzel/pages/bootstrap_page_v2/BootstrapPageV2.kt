package io.schinzel.pages.bootstrap_page_v2

import io.schinzel.pages.template_engine.TemplateRenderer

class BootstrapPageV2 {
    private var title = ""
    private val rows = mutableListOf<RowV2>()

    fun setTitle(title: String): BootstrapPageV2 {
        this.title = title
        return this
    }

    fun addRow(): BootstrapPageV2 {
        rows.add(RowV2())
        return this
    }

    fun addColumn(span: Int): BootstrapPageV2 {
        rows.last().columns.add(ColumnV2(span))
        return this
    }

    fun addPageElement(pageElement: ObservablePageElement): BootstrapPageV2 {
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