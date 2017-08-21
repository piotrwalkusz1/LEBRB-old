package com.piotrwalkusz.lebrb.wordscounter.impl

import com.piotrwalkusz.lebrb.common.Language
import com.piotrwalkusz.lebrb.wordscounter.WordsCounter
import com.piotrwalkusz.lebrb.wordscounter.WordsCounterResult
import com.piotrwalkusz.lebrb.wordscounter.dictionary.WordsRepository
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.util.PropertiesUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EnglishWordsCounter extends WordsCounter {

    private final WordsRepository wordsRepository

    private final StanfordCoreNLP pipeline

    @Autowired
    EnglishWordsCounter(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository

        pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties(
                'annotators', 'tokenize, ssplit, pos, lemma'))
    }

    @Override
    WordsCounterResult countWords(Reader reader) {
        def document = new Annotation(reader.text)

        pipeline.annotate(document)

        def dictionary = wordsRepository.getWordsByLanguage(Language.English)

        def lemmas = []
        for (def sentence : document.get(CoreAnnotations.SentencesAnnotation)) {
            for (def token : sentence.get(CoreAnnotations.TokensAnnotation)) {
                if (filterPartOfSpeech(token.get(CoreAnnotations.PartOfSpeechAnnotation))
                        && filterSpecialChars(token.get(CoreAnnotations.ValueAnnotation))) {
                    def lemma = token.get(CoreAnnotations.LemmaAnnotation)
                    if (dictionary.contains(lemma.toLowerCase())) {
                        lemmas.add(lemma)
                    }
                }
            }
        }

        def result = new WordsCounterResult()
        for (def lemma : lemmas) {
            result.addWord(lemma.toString())
        }
        return result
    }

    @Override
    Language getLanguage() {
        Language.English
    }

    private static boolean filterPartOfSpeech(String pos) {
        ['JJ', 'JJR', 'JJS', 'NN', 'NNS', 'RB', 'RBR', 'RBS', 'VB', 'VBD', 'VBG', 'VBN', 'VBP', 'VBZ', 'PRP', 'NNP',
         'NNPS'].contains(pos)
    }

    private static boolean filterSpecialChars(String token) {
        ! ['%', '<', '>'].contains(token)
    }


}
