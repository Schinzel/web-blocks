package io.schinzel.sample2.pages.user_account.update_name_pe

import io.schinzel.basicutils.RandomUtil
import io.schinzel.pages.bootstrap_page_v2.IObserverAndSubject
import io.schinzel.pages.bootstrap_page_v2.ObservablePageElement
import io.schinzel.pages.template_engine.TemplateRenderer
import io.schinzel.sample2.pages.user_account.name_pe.NameReadDao

@Suppress("CanBeParameter")
class UpdateNamePe(val userId: Int) : ObservablePageElement {
    private val firstName = NameReadDao(userId).getFirstName()
    override val guid: String = RandomUtil.getRandomString(10)
    override val observers: MutableList<IObserverAndSubject> = mutableListOf()

    override fun getHtml(): String {
        return TemplateRenderer("template.html", this)
            .addData("firstName", firstName)
            .process()
    }
}