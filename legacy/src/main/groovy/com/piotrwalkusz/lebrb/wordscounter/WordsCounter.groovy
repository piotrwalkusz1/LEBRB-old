package com.piotrwalkusz.lebrb.wordscounter

import com.piotrwalkusz.lebrb.common.Language

abstract class WordsCounter {

    abstract WordsCounterResult countWords(Reader reader)

    Language getLanguage() { null }
}