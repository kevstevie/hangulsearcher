class WordStorage(
    private val decomposer: HangulDecomposer = HangulDecomposer(),
    private val tokenizer: NGramTokenizer = NGramTokenizer()
) {

    private val tokenToWords = mutableMapOf<String, MutableSet<String>>()
    private val chosungTokenToWords = mutableMapOf<String, MutableSet<String>>()

    fun storeWord(vararg words: String) {
        for (word in words) {
            val decomposed = decomposer.decomposeToString(word)
            val tokens = tokenizer.tokenize(decomposed)
            for (token in tokens) {
                val wordSet = tokenToWords.getOrPut(token) { mutableSetOf() }
                wordSet.add(word)
            }

            val chosung = decomposer.decomposeToInitials(word)
            val chosungTokens = tokenizer.tokenize(chosung)
            for (token in chosungTokens) {
                val wordSet = chosungTokenToWords.getOrPut(token) { mutableSetOf() }
                wordSet.add(word)
            }
        }
    }

    fun searchWords(keyword: String): Set<String> {
        val decomposed = decomposer.decomposeToString(keyword)
        return tokenToWords[decomposed] ?: emptySet()
    }

    fun searchWordsForChosung(keyword: String): Set<String> {
        return chosungTokenToWords[keyword] ?: emptySet()
    }

    fun clear() {
        tokenToWords.clear()
        chosungTokenToWords.clear()
    }
}
