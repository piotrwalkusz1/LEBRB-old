package com.piotrwalkusz.lebrb.wordscounter.impl

import com.piotrwalkusz.lebrb.wordscounter.dictionary.WordsRepositoryImpl
import spock.lang.Ignore
import spock.lang.Specification

class EnglishWordsCounterTest extends Specification {

    def wordsCounter = new EnglishWordsCounter(new WordsRepositoryImpl())

    def 'empty'() {
        expect: countWords("") == [:]
    }

    def 'basic words'() {
        expect: countWords("dog cat river") == ['dog': 1, 'cat': 1, 'river': 1]
    }

    def 'unknown word'() {
        expect: countWords("unknownword") == [:]
    }

    def 'regular plural form'() {
        expect: countWords("cats kisses messages cherries") == ['cat': 1, 'kiss': 1, 'message': 1, 'cherry': 1]
    }

    def 'irregular plural form'() {
        expect: countWords('men women hooves') == ['man': 1, 'woman': 1, 'hoof': 1]
    }

    def 'case does not matter'() {
        expect: countWords('Dog Cat') == ['dog': 1, 'cat': 1]
    }

    def '"I" must be uppercase'() {
        expect: countWords('i') == [:]
    }

    @Ignore
    def 'names'() {
        expect: countWords('Alice John') == [:]
    }

    def "I'm"() {
        expect: countWords("I'm") == ['I': 1, 'be': 1]
    }

    def "isn't"() {
        expect: countWords("Isn't isn't") == ['be': 2, 'not': 2]
    }

    def "didn't"() {
        expect: countWords("Didn't didn't") == ['do': 2, 'not': 2]
    }

    def "he's"() {
        expect: countWords("He's he's") == ['he': 2, 'be': 2]
    }

    def 'double quotes'() {
        expect: countWords("Cat Dog") == ['cat': 1, 'dog': 1]
    }

    def 'interpunction'() {
        expect: countWords("cat. cat, cat? cat:cat") == ['cat': 5]
    }

    @Ignore
    def 'dash'() {
        expect: countWords("black-white") == ['black-white': 1]
    }

    def 'special chars'() {
        expect: countWords('@#$%cat*%()<>dog{}[]') == ['cat': 1, 'dog': 1]
    }

    def 'numbers'() {
        expect: countWords('10 0,9 0.9 100% 100 %') == [:]
    }

    def 'common proper nouns'() {
        expect: countWords('Sun Moon') == ['Sun': 1, 'Moon': 1]
    }

    @Ignore
    def 'countries'() {
        expect: countWords('USA Poland England') == ['USA': 1, 'Poland': 1, 'England': 1]
    }

    def countWords(String text) {
        wordsCounter.countWords(new StringReader(text)).numberOfEachWord
    }
}
