package com.piotrwalkusz.lebrb.wordscounter

import com.piotrwalkusz.lebrb.common.Language
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class WordsCounterService {

    @Autowired Collection<WordsCounter> wordsCounters

    WordsCounterResult countWords(Reader reader, Language language) {
        if (reader == null)
            throw new IllegalArgumentException('Reader must not be null')

        try {
            def wordsCounter = wordsCounters.find{it.language == language}
            if (wordsCounter) {
                return wordsCounter.countWords(reader)
            }
            else {
                throw new IllegalArgumentException("WordsCounter to $language doesn't exist")
            }
        } finally {
            reader.close()
        }
    }
}
