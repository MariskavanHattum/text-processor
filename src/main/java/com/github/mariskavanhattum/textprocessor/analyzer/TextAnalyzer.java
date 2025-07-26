package com.github.mariskavanhattum.textprocessor.analyzer;

import java.util.*;

public class TextAnalyzer implements WordFrequencyAnalyzer {
    /**
     * Calculates the highest word frequency in a text.
     * <p>
     * Frequency is case-insensitive. Words can consist of characters a-z and
     * A-Z. Any non-alphabetical character is considered separator of words.
     * @param text input text
     * @return the highest word frequency in the input text
     */
    @Override
    public int calculateHighestFrequency(String text) {
        String[] words = getWordsFromText(text);

        if (words[0].isEmpty()) {
            return 0;
        }

        List<WordInfo> wordIndex = createWordFrequencyIndexFromWords(words);

        // Return maximum of frequencies
        return wordIndex.stream()
                .mapToInt(WordInfo::getFrequency)
                .max().orElseThrow(NoSuchElementException::new);
    }

    /**
     * Calculates the frequency of a specified word in a text.
     * The input word should only contain alphabetical characters (a-z, A-Z).
     * <p>
     * Frequency is case-insensitive. Words can consist of characters a-z and
     * A-Z. Any non-alphabetical character is considered separator of words.
     * <p>
     *
     * @param text input text
     * @param word word for which the frequency is counted
     * @return the frequency of the word in the input text
     */
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

    /**
     * Calculates the n most frequent words in a text. The integer n should
     * be positive and at most the number of unique words in the input text.
     * Returns a list of the n most frequent words (in lower case) and their
     * frequencies, sorted by descending frequency and ascending alphabetical
     * order.
     * <p>
     * Frequency is case-insensitive. Words can consist of characters a-z and
     * A-Z. Any non-alphabetical character is considered a separator of words.
     * @param text input text
     * @param n number of words to return
     * @return list of the n most frequent words and their frequencies in the
     * input text
     */
    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        guardInputIntegerIsPositive(n);

        String[] words = getWordsFromText(text);

        List<WordInfo> wordIndex = createWordFrequencyIndexFromWords(words);

        guardInputIntegerAtMostWordIndexSize(n, wordIndex.size());

        // Sort wordIndex on highest frequency, then alphabetically
        wordIndex.sort(
                Comparator.comparingInt(WordInfo::getFrequency).reversed()
                        .thenComparing(WordInfo::getWord)
        );

        // Get the first n
        List<WordInfo> mostFrequentNWords = wordIndex.stream().limit(n).toList();

        return new ArrayList<>(mostFrequentNWords);
    }

    private static String[] getWordsFromText(String text) {
        /*
         * Retrieve the words from the text by splitting at any non-alphabetical
         * character, casting to lower case because frequency is case-insensitive
         */
        return text.toLowerCase().split("[^a-zA-Z]");
    }

    private static List<WordInfo> createWordFrequencyIndexFromWords(String[] words) {
        // Loop through words, adding them to a word-frequency index
        List<WordInfo> wordIndex = new ArrayList<>();
        for (String word : words) {
            boolean wordPresentInIndex = false;
            // Check if word is already present in index
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

    private static void guardInputIntegerIsPositive(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Input integer should be positive.");
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
