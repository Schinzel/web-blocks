package io.schinzel.web_blocks

import io.schinzel.sample.pages.page_with_block.greeting_block.GreetingBlock
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.intro_text.IntroductionTextBlock
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block.UpdateNamePageBlock
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.welcome_block.WelcomeBlock
import io.schinzel.web_blocks.web.routes.annotations.RouteAnnotationUtil
import io.schinzel.web_blocks.web.routes.annotations.RouteTypeEnum
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BlockAnnotationTest {
    @Test
    fun `blocks should have WebBlockPageApi annotation`() {
        // Test GreetingBlock
        val greetingBlockType = RouteAnnotationUtil.detectRouteType(GreetingBlock::class)
        assertThat(greetingBlockType).isEqualTo(RouteTypeEnum.PAGE_BLOCK)

        // Test IntroductionTextBlock
        val introBlockType = RouteAnnotationUtil.detectRouteType(IntroductionTextBlock::class)
        assertThat(introBlockType).isEqualTo(RouteTypeEnum.PAGE_BLOCK)

        // Test UpdateNameBlock
        val updateBlockType = RouteAnnotationUtil.detectRouteType(UpdateNamePageBlock::class)
        assertThat(updateBlockType).isEqualTo(RouteTypeEnum.PAGE_BLOCK)

        // Test WelcomeBlock
        val welcomeBlockType = RouteAnnotationUtil.detectRouteType(WelcomeBlock::class)
        assertThat(welcomeBlockType).isEqualTo(RouteTypeEnum.PAGE_BLOCK)
    }
}
