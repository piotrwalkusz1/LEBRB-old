package com.piotrwalkusz.lebrb.translator

import com.piotrwalkusz.lebrb.common.Language
import org.springframework.stereotype.Service

@Service
class TranslatorService {

    private final val dictionaries: Map<Language, List<String>> = Language.values().associateBy({it},
            {javaClass.getResource(it.toString().toLowerCase() + ".txt").readText().lines()})

    init {
        assert(dictionaries.values.map { it.size }.distinct().size == 1)
    }

    fun translate(words: List<String>, from: Language, to: Language): List<String> {
        return words.map { translate(it, from, to) }
    }

    fun translate(word: String, from: Language, to: Language): String {
        val index = dictionaries[from]!!.indexOf(word)
        return dictionaries[to]!!.getOrElse(index, {""})
    }
}