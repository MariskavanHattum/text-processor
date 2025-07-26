package com.github.mariskavanhattum.textprocessor.analyzer;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextAnalyzerTest {

    TextAnalyzer textAnalyzer = new TextAnalyzer();

    String exampleText = "The sun shines over the lake";
    String textWithVariousSeparatorCharacters = "a_text*with#various" +
            "|separator,characters!and.a-duplicate";
    String textWithCaseDifferingDuplicates = "hello Hello hELLo";

    @Nested
    class CalculateHighestFrequency {
        @Test
        void exampleText() {
            assertEquals(2, textAnalyzer.calculateHighestFrequency(exampleText));
        }

        @Test
        void textWithVariousSeparatorCharacters() {
            assertEquals(2,
                    textAnalyzer.calculateHighestFrequency(textWithVariousSeparatorCharacters));
        }

        @Test
        void frequencyIsCaseInsensitive() {
            assertEquals(3,
                    textAnalyzer.calculateHighestFrequency(textWithCaseDifferingDuplicates));
        }

        @Test
        void emptyText() {
            assertEquals(0, textAnalyzer.calculateHighestFrequency(""));
        }
    }

    @Nested
    class CalculateFrequencyForWord {
        @Test
        void exampleText() {
            assertEquals(2,
                    textAnalyzer.calculateFrequencyForWord(exampleText, "the"));
            assertEquals(1,
                    textAnalyzer.calculateFrequencyForWord(exampleText, "sun"));
        }

        @Test
        void textWithVariousSeparatorCharacters() {
            assertEquals(2,
                    textAnalyzer.calculateFrequencyForWord(textWithVariousSeparatorCharacters, "a"));
        }

        @Test
        void frequencyIsCaseInsensitive() {
            assertEquals(3,
                    textAnalyzer.calculateFrequencyForWord(textWithCaseDifferingDuplicates, "hello"));
        }

        @Test
        void emptyText() {
            assertEquals(0, textAnalyzer.calculateFrequencyForWord("", "absentWord"));
        }

        @Test
        void invalidInputWordThrowsException() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> textAnalyzer.calculateFrequencyForWord("text", "invalid_word"));
            assertEquals("Input word 'invalid_word' is invalid. Can only " +
                    "contain alphabetical characters a-z, A-Z.", e.getMessage());
        }
    }

    @Nested
    class CalculateMostFrequentNWords {
        @Test
        void exampleText() {
            List<WordFrequency> expected = List.of(
                    new WordInfo("the", 2),
                    new WordInfo("lake", 1),
                    new WordInfo("over", 1)
            );

            List<WordFrequency> actual =
                    textAnalyzer.calculateMostFrequentNWords(exampleText, 3);

            assertEquals(expected, actual);
        }

        @Test
        void textWithVariousSeparatorCharacters() {
            List<WordFrequency> expected = List.of(
                    new WordInfo("a", 2),
                    new WordInfo("and", 1)
            );

            List<WordFrequency> actual =
                    textAnalyzer.calculateMostFrequentNWords(textWithVariousSeparatorCharacters, 2);

            assertEquals(expected, actual);
        }

        @Test
        void frequencyIsCaseInsensitive() {
            List<WordFrequency> expected = List.of(new WordInfo("hello", 3));

            List<WordFrequency> actual =
                    textAnalyzer.calculateMostFrequentNWords(textWithCaseDifferingDuplicates, 1);

            assertEquals(expected, actual);
        }

        @Test
        void nGreaterThanNumberOfUniqueWordsThrowsException() {
            String someTextWithOneUniqueWord = "word word";

            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> textAnalyzer.calculateMostFrequentNWords(someTextWithOneUniqueWord, 2));
            assertEquals("Input integer 2 is too high. Input text" +
                    " contains 1 unique word.", e.getMessage());
        }

        @Test
        void nEqualToZeroThrowsException() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> textAnalyzer.calculateMostFrequentNWords("text", 0));
            assertEquals("Input integer should be positive.", e.getMessage());
        }

        @Test
        void nNegativeThrowsException() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> textAnalyzer.calculateMostFrequentNWords("text", -1));
            assertEquals("Input integer should be positive.", e.getMessage());
        }
    }
}


