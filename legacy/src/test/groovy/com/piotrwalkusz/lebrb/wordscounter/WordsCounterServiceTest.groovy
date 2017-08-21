package com.piotrwalkusz.lebrb.wordscounter

import com.piotrwalkusz.lebrb.common.Language
import org.junit.Test

import static org.junit.Assert.assertEquals

class WordsCounterServiceTest {

    private static class MockEnglishWordsCounter extends WordsCounter {
        @Override WordsCounterResult countWords(Reader reader) { ['dog': 1] }
        @Override Language getLanguage() { Language.English }
    }

    private static class MockGermanWordsCounter extends WordsCounter {
        @Override WordsCounterResult countWords(Reader reader) { ['hund': 1] }
        @Override Language getLanguage() { Language.German }
    }

    private def wordsCounterService = new WordsCounterService(
            wordsCounters: [new MockEnglishWordsCounter(), new MockGermanWordsCounter()])

    private def emptyReader = new StringReader('')

    @Test
    void 'WordsCounter exists'() {
        assertEquals(['dog': 1], wordsCounterService.countWords(emptyReader, Language.English).numberOfEachWord)
        assertEquals(['hund': 1], wordsCounterService.countWords(emptyReader, Language.German).numberOfEachWord)
    }

    @Test(expected = IllegalArgumentException)
    void 'WordsCounter does not exist'() {
        wordsCounterService.countWords(emptyReader, Language.Spanish)
    }
}