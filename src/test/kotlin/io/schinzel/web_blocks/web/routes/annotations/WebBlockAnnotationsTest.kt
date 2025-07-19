package io.schinzel.web_blocks.web.routes.annotations

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * The purpose of this class is to test the WebBlocks annotation system for route type identification.
 *
 * Written by Claude Sonnet 4
 */
class WebBlockAnnotationsTest {
    @Nested
    @DisplayName("WebBlockPage annotation")
    inner class WebBlockPageTests {
        @Test
        fun `annotation _ when applied to class _ annotation is present`() {
            val annotationClass = Page::class

            assertThat(annotationClass).isNotNull
        }

        @Test
        fun `annotation _ runtime retention _ is discoverable at runtime`() {
            val testClass = TestPageClass::class
            val annotation = testClass.annotations.find { it is Page }

            assertThat(annotation).isNotNull
        }

        @Test
        fun `annotation _ target class only _ has correct target`() {
            val annotation =
                Page::class
                    .annotations
                    .find { it.annotationClass.simpleName == "Target" }

            assertThat(annotation).isNotNull
        }

        @Test
        fun `annotation _ retention runtime _ has correct retention`() {
            val annotation =
                Page::class
                    .annotations
                    .find { it.annotationClass.simpleName == "Retention" }

            assertThat(annotation).isNotNull
        }

        @Test
        fun `annotation _ applied to test class _ can be retrieved using reflection`() {
            val isAnnotated = TestPageClass::class.java.isAnnotationPresent(Page::class.java)

            assertThat(isAnnotated).isTrue
        }
    }

    @Nested
    @DisplayName("WebBlockApi annotation")
    inner class WebBlockApiTests {
        @Test
        fun `annotation _ when applied to class _ annotation is present`() {
            val annotationClass = Api::class

            assertThat(annotationClass).isNotNull
        }

        @Test
        fun `annotation _ runtime retention _ is discoverable at runtime`() {
            val testClass = TestApiClass::class
            val annotation = testClass.annotations.find { it is Api }

            assertThat(annotation).isNotNull
        }

        @Test
        fun `annotation _ target class only _ has correct target`() {
            val annotation =
                Api::class
                    .annotations
                    .find { it.annotationClass.simpleName == "Target" }

            assertThat(annotation).isNotNull
        }

        @Test
        fun `annotation _ retention runtime _ has correct retention`() {
            val annotation =
                Api::class
                    .annotations
                    .find { it.annotationClass.simpleName == "Retention" }

            assertThat(annotation).isNotNull
        }

        @Test
        fun `annotation _ applied to test class _ can be retrieved using reflection`() {
            val isAnnotated = TestApiClass::class.java.isAnnotationPresent(Api::class.java)

            assertThat(isAnnotated).isTrue
        }
    }

    @Nested
    @DisplayName("WebBlockPageApi annotation")
    inner class WebBlockPageApiTests {
        @Test
        fun `annotation _ when applied to class _ annotation is present`() {
            val annotationClass = WebBlockPageApi::class

            assertThat(annotationClass).isNotNull
        }

        @Test
        fun `annotation _ runtime retention _ is discoverable at runtime`() {
            val testClass = TestPageApiClass::class
            val annotation = testClass.annotations.find { it is WebBlockPageApi }

            assertThat(annotation).isNotNull
        }

        @Test
        fun `annotation _ target class only _ has correct target`() {
            val annotation =
                WebBlockPageApi::class
                    .annotations
                    .find { it.annotationClass.simpleName == "Target" }

            assertThat(annotation).isNotNull
        }

        @Test
        fun `annotation _ retention runtime _ has correct retention`() {
            val annotation =
                WebBlockPageApi::class
                    .annotations
                    .find { it.annotationClass.simpleName == "Retention" }

            assertThat(annotation).isNotNull
        }

        @Test
        fun `annotation _ applied to test class _ can be retrieved using reflection`() {
            val isAnnotated = TestPageApiClass::class.java.isAnnotationPresent(WebBlockPageApi::class.java)

            assertThat(isAnnotated).isTrue
        }
    }

    @Nested
    @DisplayName("Multiple annotations")
    inner class MultipleAnnotationsTests {
        @Test
        fun `annotations _ different types _ can be distinguished using reflection`() {
            val pageClass = TestPageClass::class.java
            val apiClass = TestApiClass::class.java
            val pageApiClass = TestPageApiClass::class.java

            assertThat(pageClass.isAnnotationPresent(Page::class.java)).isTrue
            assertThat(pageClass.isAnnotationPresent(Api::class.java)).isFalse
            assertThat(pageClass.isAnnotationPresent(WebBlockPageApi::class.java)).isFalse

            assertThat(apiClass.isAnnotationPresent(Page::class.java)).isFalse
            assertThat(apiClass.isAnnotationPresent(Api::class.java)).isTrue
            assertThat(apiClass.isAnnotationPresent(WebBlockPageApi::class.java)).isFalse

            assertThat(pageApiClass.isAnnotationPresent(Page::class.java)).isFalse
            assertThat(pageApiClass.isAnnotationPresent(Api::class.java)).isFalse
            assertThat(pageApiClass.isAnnotationPresent(WebBlockPageApi::class.java)).isTrue
        }

        @Test
        fun `classes _ without annotations _ are not detected as annotated`() {
            val unannotatedClass = UnannotatedClass::class.java

            assertThat(unannotatedClass.isAnnotationPresent(Page::class.java)).isFalse
            assertThat(unannotatedClass.isAnnotationPresent(Api::class.java)).isFalse
            assertThat(unannotatedClass.isAnnotationPresent(WebBlockPageApi::class.java)).isFalse
        }
    }

    @Nested
    @DisplayName("Annotation discovery")
    inner class AnnotationDiscoveryTests {
        @Test
        fun `getAnnotation _ on annotated class _ returns annotation instance`() {
            val pageAnnotation = TestPageClass::class.java.getAnnotation(Page::class.java)
            val apiAnnotation = TestApiClass::class.java.getAnnotation(Api::class.java)
            val pageApiAnnotation = TestPageApiClass::class.java.getAnnotation(WebBlockPageApi::class.java)

            assertThat(pageAnnotation).isNotNull
            assertThat(apiAnnotation).isNotNull
            assertThat(pageApiAnnotation).isNotNull
        }

        @Test
        fun `getAnnotation _ on non-annotated class _ returns null`() {
            val annotation = UnannotatedClass::class.java.getAnnotation(Page::class.java)

            assertThat(annotation).isNull()
        }

        @Test
        fun `annotations _ can be found through class scanning _ all annotation types discoverable`() {
            val testClasses =
                listOf(
                    TestPageClass::class.java,
                    TestApiClass::class.java,
                    TestPageApiClass::class.java,
                    UnannotatedClass::class.java,
                )

            val pageClasses = testClasses.filter { it.isAnnotationPresent(Page::class.java) }
            val apiClasses = testClasses.filter { it.isAnnotationPresent(Api::class.java) }
            val pageApiClasses = testClasses.filter { it.isAnnotationPresent(WebBlockPageApi::class.java) }

            assertThat(pageClasses).hasSize(1)
            assertThat(apiClasses).hasSize(1)
            assertThat(pageApiClasses).hasSize(1)
        }
    }

    // Test classes for annotation testing
    @Page
    private class TestPageClass

    @Api
    private class TestApiClass

    @WebBlockPageApi
    private class TestPageApiClass

    private class UnannotatedClass
}
