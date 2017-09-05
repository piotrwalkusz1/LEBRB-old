package com.piotrwalkusz.lebrb.wordscounter

import com.piotrwalkusz.lebrb.common.Language
import java.io.BufferedReader

abstract class WordsCounter(val language: Language) {

    abstract fun countWords(reader: BufferedReader): WordsCounterResult
}