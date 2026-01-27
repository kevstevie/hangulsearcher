class WordSearchEngine(
    private val storage: WordStorage = MemoryWordStorage(),
    private val decomposer: HangulDecomposer = HangulDecomposer(),
    private val tokenizer: NGramTokenizer = NGramTokenizer()
) {

    fun storeWord(vararg words: String) {
        for (word in words) {
            if (word.isEmpty()) continue

            val decomposed = decomposer.decomposeToString(word)
            val tokens = tokenizer.tokenize(decomposed)
            for (token in tokens) {
                storage.storeToken(token, word)
            }

            val chosung = decomposer.decomposeToInitials(word)
            val chosungTokens = tokenizer.tokenize(chosung)
            for (token in chosungTokens) {
                storage.storeChosungToken(token, word)
            }
        }
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
