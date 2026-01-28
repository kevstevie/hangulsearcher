package com.korassemble.tokenizer

class NGramTokenizer {
    fun tokenize(text: String): List<String> {
        val tokens = mutableListOf<String>()
        val len = text.length

        val tokenBuilder = StringBuilder()
        for (i in 0 until len) {
            for (j in i until len) {
                tokenBuilder.append(text[j])
                tokens.add(tokenBuilder.toString())
            }
            tokenBuilder.clear()
        }

        return tokens
    }
}