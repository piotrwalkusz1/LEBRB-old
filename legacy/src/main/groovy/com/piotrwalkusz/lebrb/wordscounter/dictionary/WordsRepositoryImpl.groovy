package com.piotrwalkusz.lebrb.wordscounter.dictionary

import com.piotrwalkusz.lebrb.common.Language
import org.springframework.stereotype.Component

@Component
class WordsRepositoryImpl implements WordsRepository {

    private final Map<Language, List<String>> dictionaries = [:]

    WordsRepositoryImpl() {
        Map<Language, String> files = [:]
        files.put(Language.English, 'english.txt')
        files.put(Language.German, 'german.txt')
        files.put(Language.Polish, 'polish.txt')

        for (def file : files) {
            def dictionary = new ArrayList<String>()
            getClass().getResource(file.value).eachLine { dictionary.add(it) }
            dictionaries.put(file.key, dictionary)
        }
    }

    @Override
    List<String> getWordsByLanguage(Language language) {
        Collections.unmodifiableList(dictionaries[language])
    }
}
