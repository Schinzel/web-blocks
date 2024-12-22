package io.schinzel.sample.pages.user_account

import io.schinzel.page_elements.IPage
import io.schinzel.pages.bootstrap_page.BootstrapPage
import io.schinzel.sample.pages.user_account.my_pe.MyPe
import io.schinzel.sample.pages.user_account.name_pe.NamePe

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