package com.piotrwalkusz.lebrb.wordscounter

import org.junit.Test

import static org.junit.Assert.*

class WordsCounterResultTest {

    def result = new WordsCounterResult()

    @Test
    void 'empty'() {
        assertEquals([:], result.numberOfEachWord)
        assertEquals(0, result.totalNumberOfWords)
    }

    @Test
    void 'initialization'() {
        result = new WordsCounterResult(['a': 1, 'b': 2])

        assertEquals(['a': 1, 'b': 2], result.numberOfEachWord)
        assertEquals(3, result.totalNumberOfWords)
    }

    @Test
    void 'add new word'() {
        result = new WordsCounterResult(['a': 1])

        result.addWord('b')

        assertEquals(['a': 1, 'b': 1], result.numberOfEachWord)
        assertEquals(2, result.totalNumberOfWords)
    }

    @Test
    void 'add the same word'() {
        result = new WordsCounterResult(['a': 1])

        result.addWord('a')

        assertEquals(['a': 2], result.numberOfEachWord)
        assertEquals(2, result.totalNumberOfWords)
    }

    @Test
    void 'get number of each words sorted desc'() {
        result = new WordsCounterResult(['a': 2, 'b': 3, 'c': 1])

        def sorted = result.getNumberOfEachWordSortedDesc()

        assertEquals([3, 2, 1], sorted.values().toList())
        assertEquals(['b', 'a', 'c'], sorted.keySet().toList())
    }

    @Test(expected = UnsupportedOperationException)
    void 'get number of each words return unmodifiable map'() {
        result = new WordsCounterResult(['a': 2, 'b': 3, 'c': 1])

        def numberOfEachWords = result.getNumberOfEachWord()

        numberOfEachWords.put('d', 1)
    }

    @Test(expected = UnsupportedOperationException)
    void 'get number of each words sorted desc return unmodifiable map'() {
        result = new WordsCounterResult(['a': 2, 'b': 3, 'c': 1])

        def numberOfEachWords = result.getNumberOfEachWordSortedDesc()

        numberOfEachWords.put('d', 1)
    }
}