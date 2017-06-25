package com.piotrwalkusz.lebrb.wordscounter;

import org.openjdk.jmh.annotations.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@State(Scope.Benchmark)
@Warmup(iterations = 20)
@Fork(1)
public class WordsCounterBenchmark {

    private WordsCounter wordsCounter;

    @Setup
    public void start() throws java.io.IOException {
        // TODO
//        DictionaryProvider dictionaryProvider = new FileDictionaryProvider(
//               new BufferedReader(new InputStreamReader(getClass().getResource("/dictionary_test.txt").openStream())));
//        wordsCounter = new WordsCounterBase(dictionaryProvider, new EmptyWordsTransformsProvider());
    }

    @Benchmark
    public Map<String, Integer> basicWords() {
        return countWords("/book_basic_words.txt");
    }

    @Benchmark
    public Map<String, Integer> derivativeWords() {
        return countWords("/book_derivative_words.txt");
    }

    @Benchmark
    public Map<String, Integer> unknowedWords() {
        return countWords("/book_unknown_words.txt");
    }

    private Map<String, Integer> countWords(String path) {
        try (Reader book = new BufferedReader(new InputStreamReader(getClass().getResource(path).openStream()))) {
            return wordsCounter.countWords(book).getNumberOfEachWord();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}