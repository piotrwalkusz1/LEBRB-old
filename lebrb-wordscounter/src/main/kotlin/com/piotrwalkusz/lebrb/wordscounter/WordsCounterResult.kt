package com.piotrwalkusz.lebrb.wordscounter

import kotlin.collections.HashMap

class WordsCounterResult(numberOfEachWord: Map<String, Int> = emptyMap()) {

    var totalNumberOfWords = numberOfEachWord.values.sum()
        private set

    private val numberOfEachWord = HashMap(numberOfEachWord)

    fun getNumberOfEachWordSortedDesc(): Map<String, Int> =
            numberOfEachWord.entries.sortedByDescending { it.value }.associateBy({it.key}, {it.value})

    fun addWord(word: String) {
        numberOfEachWord.compute(word, { _, v -> if (v == null) 1 else v + 1 })
        totalNumberOfWords++
    }

    fun getCoverage(maxNumberOfDistinctWords: Int): List<Float> {
        val numbers = numberOfEachWord.values.sortedDescending()
        val coverage = mutableListOf<Float>()
        var sum = 0.0F
        for (n in numbers) {
            sum += n
            coverage.add(sum / totalNumberOfWords)
        }
        return coverage
    }
}