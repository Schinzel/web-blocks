package io.schinzel.page_elements

import kotlin.random.Random

interface IPageElement2 {

    fun getHtml(): String
}

interface IColumn {
    val pageElements: List<IPageElement2>

    fun getHtml(): String
}

interface IRow {
    val columns: List<IColumn>

    fun getHtmlSurroundingRow()

    fun getHtml(): String
}

interface IPage2 {
    val rows: List<IRow>

    fun getHtml(): String
}


interface IPageElement3 {

    fun getMyHtml(): String{
        return "<div>MyHtml</div>"
    }

    fun getHtml(): String
}

/*
* Each element has a guid and an URL to update itself
 */