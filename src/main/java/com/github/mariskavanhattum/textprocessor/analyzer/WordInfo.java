package com.github.mariskavanhattum.textprocessor.analyzer;

public class WordInfo implements WordFrequency {
    private final String word;
    private int frequency;

    public WordInfo(String word) {
        this.word = word;
        this.frequency = 1;
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
}
