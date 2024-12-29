package io.schinzel.sample2.pages.user_account.intro_text

import io.schinzel.basicutils.RandomUtil
import io.schinzel.pages.bootstrap_page_v2.IObserverAndSubject
import io.schinzel.pages.bootstrap_page_v2.ObservablePageElement
import io.schinzel.pages.template_engine.TemplateRenderer
import io.schinzel.sample2.pages.user_account.NameDao

class IntroductionTextPe (val userId: Int) : ObservablePageElement {
    override val guid: String = RandomUtil.getRandomString(10)
    override val observers = mutableListOf<IObserverAndSubject>()

    private val firstName = NameDao(userId).getFirstName()
    override fun getResponse(): String {
        return TemplateRenderer("template.html", this)
            .addData("firstName", firstName)
            .process()
    }
}