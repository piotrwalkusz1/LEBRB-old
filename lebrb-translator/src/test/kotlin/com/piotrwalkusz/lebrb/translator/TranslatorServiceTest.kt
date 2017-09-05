package com.piotrwalkusz.lebrb.translator

import com.piotrwalkusz.lebrb.common.Language
import org.junit.Test

import org.junit.Assert.*

class TranslatorServiceTest {

    private val translator = TranslatorService()

    @Test
    fun translate_one_word() {
        assertEquals("skok", translator.translate("jump", Language.ENGLISH, Language.POLISH))
        assertEquals("", translator.translate("unexistingword", Language.ENGLISH, Language.POLISH))
    }

    @Test
    fun translate_many_words() {
        val result = translator.translate(listOf("jump", "dog", "unexistingword"), Language.ENGLISH, Language.POLISH)
        assertArrayEquals(arrayOf("skok", "pies", ""), result.toTypedArray())
    }
}