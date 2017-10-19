package org.wordpress.aztec.demo.tests

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.wordpress.aztec.demo.BaseTest
import org.wordpress.aztec.demo.MainActivity
import org.wordpress.aztec.demo.pages.EditorPage

class MixedTextFormattingTests : BaseTest() {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testBoldAndItalicFormatting() {
        val text1 = "so"
        val text2 = "me "
        val text3 = "text "
        val html = "<b>$text1</b><i>$text2</i><b><i>$text3</i></b>"

        EditorPage()
                .toggleBold()
                .insertText(text1)
                .toggleBold()
                .toggleItalics()
                .insertText(text2)
                .toggleBold()
                .insertText(text3)
                .toggleHtml()
                .verifyHTML(html)
    }

    @Test
    fun testRemoveFormattingAndContinueTyping() {
        val text1 = "some"
        val text2 = "more"
        val text3 = "text"
        val html = "$text1<b>$text2</b>$text3"

        EditorPage()
                .insertText(text1)
                .toggleBold()
                .insertText(text2)
                .toggleBold()
                .insertText(text3)
                .toggleHtml()
                .verifyHTML(html)
    }

    @Test
    fun testParagraphAndBlockFormatting() {
        val text = "some text"
        val html1 = "<p>$text</p>"
        val html2 = "<blockquote>$text</blockquote>"

        EditorPage()
                .toggleHtml()
                .insertHTML(html1)
                .toggleHtml()
                .toggleQuote()
                .toggleHtml()
                .verifyHTML(html2)
    }

    @Test
    fun testRetainParagraphFormatting() {
        val text = "some text"
        val html = "<p>$text</p>"

        EditorPage()
                .toggleHtml()
                .insertHTML(html)
                .toggleHtml()
                .toggleHtml()
                .verifyHTML(html)
    }

    @Test
    fun testRetainHeadingFormatting() {
        val text = "some text"
        val html = "<h1>$text</h1>"

        EditorPage()
                .toggleHtml()
                .insertHTML(html)
                .toggleHtml()
                .selectAllText()
                .makeHeader(EditorPage.HeadingStyle.ONE)
                .toggleHtml()
                .verifyHTML(html)
    }

    @Test
    fun testSwitchHeadingFormatting() {
        val text = "some text"
        val html = "<h1>$text</h1>"

        EditorPage()
                .toggleHtml()
                .insertHTML(html)
                .toggleHtml()
                .selectAllText()
                .makeHeader(EditorPage.HeadingStyle.DEFAULT)
                .toggleHtml()
                .verifyHTML(text)
    }

    // Test reproducing the issue described in
    // https://github.com/wordpress-mobile/AztecEditor-Android/pull/476#issuecomment-327762497
    @Test
    fun testMixedBoldItalics() {
        val regex = Regex("<b>a</b><i>b </i><[bi]><[bi]>c </[bi]></[bi]>")

        EditorPage()
                .toggleBold()
                .insertText("a")
                .toggleBold()
                .toggleItalics()
                .insertText("b")
                .insertText(" ")
                .toggleBold()
                .insertText("c")
                .insertText(" ")
                .toggleHtml()
                .verifyHTML(regex)
    }
}
