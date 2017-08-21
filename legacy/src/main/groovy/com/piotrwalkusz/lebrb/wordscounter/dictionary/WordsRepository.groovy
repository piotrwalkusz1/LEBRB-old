package com.piotrwalkusz.lebrb.wordscounter.dictionary

import com.piotrwalkusz.lebrb.common.Language

interface WordsRepository {

    List<String> getWordsByLanguage(Language language)
}