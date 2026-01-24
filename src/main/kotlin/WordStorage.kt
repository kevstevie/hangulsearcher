class WordStorage(private val decomposer: HangulDecomposer = HangulDecomposer()) {

    private val tokenToWords = mutableMapOf<String, MutableSet<String>>()
    private val chosungTokenToWords = mutableMapOf<String, MutableSet<String>>()

    fun storeWord(vararg words: String) {
        for (i in words) {
            val decomposed = decomposer.decomposeToString(i)
            val tokens = tokenize(decomposed)
            for (token in tokens) {
                val wordSet = tokenToWords.getOrPut(token) { mutableSetOf() }
                wordSet.add(i)
            }

            val chosung = decomposer.decomposeToInitials(i)
            val chosungTokens = tokenize(chosung)
            for (token in chosungTokens) {
                val wordSet = chosungTokenToWords.getOrPut(token) { mutableSetOf() }
                wordSet.add(i)
            }
        }
    }

    private fun tokenize(decomposed: String): List<String> {
        val result = mutableListOf<String>()
        val len = decomposed.length

        for (i in 1..len) {
            for (j in 0..len - i) {
                result.add(decomposed.substring(j, j + i))
            }
        }

        return result
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
