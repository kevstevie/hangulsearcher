package com.korassemble.search

import com.korassemble.core.HangulDecomposer
import com.korassemble.storage.MemoryWordStorage
import com.korassemble.storage.WordStorage
import com.korassemble.tokenizer.NGramTokenizer
import com.korassemble.tokenizer.TokenizedWord

class WordSearchEngine(
    private val storage: WordStorage = MemoryWordStorage(),
    private val decomposer: HangulDecomposer = HangulDecomposer(),
    private val tokenizer: NGramTokenizer = NGramTokenizer()
) {

    fun storeWords(vararg words: String) {
        storeWordsByTokens(*words)
        storeWordsByChosungTokens(*words)
    }

    fun storeWordsByTokens(vararg words: String) {
        val tokenizedWords = mutableListOf<TokenizedWord>()
        for (word in words) {
            if (word.isEmpty()) continue

            val decomposed = decomposer.decomposeToString(word)
            val tokenized = tokenizer.tokenize(decomposed)
            tokenizedWords.add(TokenizedWord(word, tokenized))
        }
        storage.saveAllWordWithTokens(tokenizedWords)
    }

    fun storeWordsByChosungTokens(vararg words: String) {
        val tokenizedWords = mutableListOf<TokenizedWord>()
        for (word in words) {
            if (word.isEmpty()) continue

            val chosung = decomposer.decomposeToInitials(word)
            val chosungTokenized = tokenizer.tokenize(chosung)
            tokenizedWords.add(TokenizedWord(word, chosungTokenized))
        }
        storage.saveAllWordWithChosungTokens(tokenizedWords)
    }

    fun searchWords(keyword: String): Set<String> {
        if (keyword.isEmpty()) return emptySet()
        val decomposed = decomposer.decomposeToString(keyword)
        return storage.getWordsByToken(decomposed)
    }

    fun searchWordsForChosung(keyword: String): Set<String> {
        if (keyword.isEmpty()) return emptySet()
        return storage.getWordsByChosungToken(keyword)
    }

    fun clear() {
        storage.clear()
    }
}