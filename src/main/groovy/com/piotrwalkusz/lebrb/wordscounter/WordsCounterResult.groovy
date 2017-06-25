package com.piotrwalkusz.lebrb.wordscounter

class WordsCounterResult {

    final Map<String, Integer> numberOfEachWord

    private int totalNumberOfWords

    WordsCounterResult() {
        this ([:])
    }

    WordsCounterResult(Map<String, Integer> numberOfEachWord) {
        this.numberOfEachWord = new HashMap(numberOfEachWord)
        this.totalNumberOfWords = numberOfEachWord.values().sum(0) as int
    }

    int getTotalNumberOfWords() {
        totalNumberOfWords
    }

    Map<String, Integer> getNumberOfEachWord() {
        Collections.unmodifiableMap(numberOfEachWord)
    }

    Map<String, Integer> getNumberOfEachWordSortedDesc() {
        Collections.unmodifiableMap(numberOfEachWord.toSorted({-it.value}))
    }

    void addWord(String word) {
        numberOfEachWord.compute(word, { k, v -> v == null ? 1 : v + 1 })
        totalNumberOfWords++
    }

    List<Float> getCoverage(int maxNumberOfDistinctWords) {
        def numbers = numberOfEachWord.values().sort {a, b -> b <=> a}.take(maxNumberOfDistinctWords)
        def coverage = []
        def sum = 0
        for (def n : numbers) {
            sum += n
            coverage << sum / totalNumberOfWords
        }
        return coverage
    }
}
