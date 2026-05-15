package com.example.aggregator.service;

import com.example.aggregator.client.AggregatorRestClient;
import com.example.aggregator.model.Entry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AggregatorService {

    private AggregatorRestClient restClient;

    public AggregatorService(AggregatorRestClient restClient) {
        this.restClient = restClient;
    }

    public Entry getDefinitionFor(String word) {
        return restClient.getDefinitionFor(word);
    }

    public List<Entry> getWordsThatContainSuccessiveLettersAndStartsWith(String chars) {

        List<Entry> wordsThatStartWith = restClient.getWordsStartingWith(chars);
        List<Entry> wordsThatContainSuccessiveLetters = restClient.getWordsThatContainConsecutiveLetters();

        List<Entry> common = new ArrayList<>(wordsThatStartWith);
        common.retainAll(wordsThatContainSuccessiveLetters);

        return common;
    }

    public List<Entry> getWordsThatContainSuccessiveLettersAndContains(String chars) {

        List<Entry> wordsThatContain = restClient.getWordsThatContain(chars);
        List<Entry> wordsThatContainSuccessiveLetters = restClient.getWordsThatContainConsecutiveLetters();

        List<Entry> common = new ArrayList<>(wordsThatContain);
        common.retainAll(wordsThatContainSuccessiveLetters);

        return common;
    }

    public List<Entry> getAllPalindromes() {

        List<Entry> candidates = new ArrayList<>();

        // Build array of letters a-z
        char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        // Iterate over each letter
        for (char ch : letters) {
            String c = Character.toString(ch);

            // Get words starting and ending with this letter
            List<Entry> startsWith = restClient.getWordsStartingWith(c);
            List<Entry> endsWith = restClient.getWordsEndingWith(c);

            // Keep only entries that appear in both lists
            List<Entry> startsAndEndsWith = new ArrayList<>(startsWith);
            startsAndEndsWith.retainAll(endsWith);

            candidates.addAll(startsAndEndsWith);
        }

        // Filter for palindromes
        List<Entry> palindromes = new ArrayList<>();
        for (Entry entry : candidates) {
            String word = entry.getWord();
            String reverse = new StringBuilder(word).reverse().toString();
            if (word.equals(reverse)) {
                palindromes.add(entry);
            }
        }

        // Sort and return
        Collections.sort(palindromes);
        return palindromes;
    }
}