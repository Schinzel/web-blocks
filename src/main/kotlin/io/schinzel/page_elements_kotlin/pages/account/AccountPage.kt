package io.schinzel.page_elements_kotlin.pages.account

import io.schinzel.page_elements_kotlin.pages.account.my_pe.MyPe
import io.schinzel.page_elements_kotlin.pages.account.name_pe.NamePe
import io.schinzel.page_elements_kotlin.stuff.IPage
import io.schinzel.page_elements_kotlin.stuff.find_pages.annotations.Page
import io.schinzel.page_elements_kotlin.stuff.bootstrap_page.BootstrapPage

@Page
class AccountPage: IPage {

    override fun getHtml(): String {
        return BootstrapPage()
            .setTitle("Account")

            .addRow()
            .addColumn(6)
            .addPageElement(MyPe("Row 1 Column 1"))
            .addPageElement(MyPe("Row 1 Column 1"))
            .addColumn(6)
            .addPageElement(MyPe("Row 1 Column 2"))
            .addPageElement(MyPe("Row 1 Column 2"))

            .addRow()
            .addColumn(2)
            .addPageElement(MyPe("Row 2 Column 1"))
            .addColumn(5)
            .addPageElement(MyPe("Row 2 Column 2"))
            .addPageElement(MyPe("Row 2 Column 2"))
            .addColumn(5)
            .addPageElement(NamePe(1))
            .getHtml()
    }
}