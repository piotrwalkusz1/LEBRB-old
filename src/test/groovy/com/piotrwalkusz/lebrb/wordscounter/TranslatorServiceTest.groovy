package com.piotrwalkusz.lebrb.wordscounter

import com.piotrwalkusz.lebrb.common.Language
import com.piotrwalkusz.lebrb.wordscounter.dictionary.WordsRepositoryImpl

import static org.junit.Assert.*
import org.junit.Test

class TranslatorServiceTest {

    private static def translator = new TranslatorService(new WordsRepositoryImpl())

    @Test
    void 'translate'() {
        assertEquals('pies', translator.translate('dog', Language.English, Language.Polish))
        assertEquals('kot', translator.translate('cat', Language.English, Language.Polish))
        assertEquals('biegać', translator.translate('run', Language.English, Language.Polish))
    }

    @Test
    void 'null if translation is impossible'() {
        assertNull('pies', translator.translate('nonexistingword', Language.English, Language.Polish))
    }

    @Test
    void 'ignore case'() {
        assertEquals('pies', translator.translate('Dog', Language.English, Language.Polish))
        assertEquals('ja', translator.translate('I', Language.English, Language.Polish))
        assertEquals('ja', translator.translate('i', Language.English, Language.Polish))
    }
}
