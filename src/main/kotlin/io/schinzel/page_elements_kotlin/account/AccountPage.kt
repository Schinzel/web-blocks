package io.schinzel.page_elements_kotlin.account

import io.schinzel.page_elements_kotlin.account.my_pe.MyPe
import io.schinzel.stuff.bootstrap_page.BootstrapPage

class AccountPage {

    fun getHtml(): String {
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
            .addPageElement(MyPe("Row 2 Column 3"))
            .getHtml()
    }
}