package com.github.mariskavanhattum.textprocessor.analyzer;

import java.util.*;

public final class TextAnalyzer implements WordFrequencyAnalyzer {
    @Override
    public int calculateHighestFrequency(String text) {
        String[] words = getWordsFromText(text);

        ArrayList<WordInfo> wordIndex = createWordFrequencyIndexFromWords(words);

        // Return maximum of frequencies
        return wordIndex.stream()
                .mapToInt(WordInfo::getFrequency)
                .max().orElseThrow(NoSuchElementException::new);
    }

    @Override
    public int calculateFrequencyForWord(String text, String word) {
        guardInputWordIsValid(word);

        String[] words = getWordsFromText(text);

        int frequency = 0;
        for (String wordInText : words) {
            if (wordInText.equals(word)) {
                frequency++;
            }
        }

        return frequency;
    }

    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        String[] words = getWordsFromText(text);

        ArrayList<WordInfo> wordIndex = createWordFrequencyIndexFromWords(words);

        guardInputIntegerAtMostWordIndexSize(n, wordIndex.size());

        wordIndex.sort(
                Comparator.comparingInt(WordInfo::getFrequency).reversed()
                        .thenComparing(WordInfo::getWord)
        );

        List<WordInfo> list = wordIndex.stream().limit(n).toList();

        return new ArrayList<>(list);
    }

    private static String[] getWordsFromText(String text) {
        // Retrieve the words from the text by splitting at any
        // non-alphabetical character, casting to lower case because
        // frequency is case-insensitive
        return text.toLowerCase().split("[^a-zA-Z]");
    }

    private static ArrayList<WordInfo> createWordFrequencyIndexFromWords(String[] words) {
        // Loop through words, adding them to a word-frequency index
        ArrayList<WordInfo> wordIndex = new ArrayList<>();
        for (String word : words) {
            boolean wordPresentInIndex = false;
            for (WordInfo wordInfo : wordIndex) {
                // If word is present in index, increment the frequency
                if (wordInfo.getWord().equals(word)) {
                    wordInfo.incrementFrequency();
                    wordPresentInIndex = true;
                    break;
                }
            }

            // If word is not present in index, add word to index
            if (!wordPresentInIndex) {
                wordIndex.add(new WordInfo(word));
            }
        }
        return wordIndex;
    }

    private static void guardInputWordIsValid(String word) {
        if (!word.matches("^[a-zA-Z]*$")) {
            throw new IllegalArgumentException("Input word '" + word + "' is " +
                    "invalid. Can only contain alphabetical characters a-z, " +
                    "A-Z.");
        }
    }

    private static void guardInputIntegerAtMostWordIndexSize(int n,
                                                             int indexSize) {
        if (n > indexSize) {
            String message = "Input integer " + n + " is too high. Input text" +
                    " contains " + indexSize + " unique word" +
                    (indexSize > 1 ? "s" : "") + ".";
            throw new IllegalArgumentException(message);
        }
    }
}
