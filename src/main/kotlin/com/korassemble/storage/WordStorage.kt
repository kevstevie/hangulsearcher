package com.korassemble.storage

import com.korassemble.tokenizer.TokenizedWord

interface WordStorage {
    fun saveWordWithTokens(word: TokenizedWord)
    fun saveAllWordWithTokens(words: List<TokenizedWord>)
    fun getWordsByToken(token: String): Set<String>
    fun saveWordWithChosungTokens(word: TokenizedWord)
    fun saveAllWordWithChosungTokens(words: List<TokenizedWord>)
    fun getWordsByChosungToken(token: String): Set<String>
    fun clear()
}