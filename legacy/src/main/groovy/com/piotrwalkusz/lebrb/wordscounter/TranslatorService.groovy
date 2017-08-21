package com.piotrwalkusz.lebrb.wordscounter

import com.piotrwalkusz.lebrb.common.Language
import com.piotrwalkusz.lebrb.wordscounter.dictionary.WordsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TranslatorService {

    private final WordsRepository wordsRepository

    @Autowired
    TranslatorService(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository
    }

    String translate(String word, Language from, Language to) {
        int index = wordsRepository.getWordsByLanguage(from).indexOf(word.toLowerCase())
        index == -1 ? null : wordsRepository.getWordsByLanguage(to)[index]
    }
}
