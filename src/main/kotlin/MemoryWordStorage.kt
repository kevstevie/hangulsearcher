class MemoryWordStorage : WordStorage {

    private val tokenToWords = mutableMapOf<String, MutableSet<String>>()
    private val chosungTokenToWords = mutableMapOf<String, MutableSet<String>>()

    override fun storeWordWithTokens(tokens: List<String>, word: String) {
        for (token in tokens) {
            val wordSet = tokenToWords.getOrPut(token) { mutableSetOf() }
            wordSet.add(word)
        }
    }

    override fun getWordsByToken(token: String): Set<String> {
        return tokenToWords[token] ?: emptySet()
    }

    override fun storeWordWithChosungTokens(tokens: List<String>, word: String) {
        for (token in tokens) {
            val wordSet = chosungTokenToWords.getOrPut(token) { mutableSetOf() }
            wordSet.add(word)
        }
    }

    override fun getWordsByChosungToken(token: String): Set<String> {
        return chosungTokenToWords[token] ?: emptySet()
    }

    override fun clear() {
        tokenToWords.clear()
        chosungTokenToWords.clear()
    }
}
