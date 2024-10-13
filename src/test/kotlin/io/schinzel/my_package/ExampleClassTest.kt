package io.schinzel.my_package

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ExampleClassTest {

    @Test
    fun testIt() {
        val actual = ExampleClass().doubleIt(4)
        val expected = 8
        assertThat(actual).isEqualTo(expected)
    }
    @Test
    fun testIt2() {
        val actual = ExampleClass().doubleIt(4)
        val expected = 8
        assertThat(actual).isEqualTo(expected)
    }
    @Nested
    inner class NestedClass {

    }
}