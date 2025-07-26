package com.github.mariskavanhattum.textprocessor.analyzer;

import java.util.Objects;

public class WordInfo implements WordFrequency {
    private final String word;
    private int frequency;

    public WordInfo(String word) {
        this.word = word;
        this.frequency = 1;
    }

    public WordInfo(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public void incrementFrequency() {
        frequency++;
    }

    @Override
    public String getWord() {
        return word;
    }

    @Override
    public int getFrequency() {
        return frequency;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        WordInfo wordInfo = (WordInfo) obj;
        return Objects.equals(this.word, wordInfo.word) &&
                Objects.equals(this.frequency, wordInfo.frequency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, frequency);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class WordInfo {\n");
        sb.append("    word: ").append(word).append("\n");
        sb.append("    frequency: ").append(frequency).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
