class MemoryWordStorage : WordStorage {

    private val tokenToWords = mutableMapOf<String, MutableSet<String>>()
    private val chosungTokenToWords = mutableMapOf<String, MutableSet<String>>()

    override fun storeToken(token: String, word: String) {
        val wordSet = tokenToWords.getOrPut(token) { mutableSetOf() }
        wordSet.add(word)
    }

    override fun getWordsByToken(token: String): Set<String> {
        return tokenToWords[token] ?: emptySet()
    }

    override fun storeChosungToken(token: String, word: String) {
        val wordSet = chosungTokenToWords.getOrPut(token) { mutableSetOf() }
        wordSet.add(word)
    }

    override fun getWordsByChosungToken(token: String): Set<String> {
        return chosungTokenToWords[token] ?: emptySet()
    }

    override fun clear() {
        tokenToWords.clear()
        chosungTokenToWords.clear()
    }
}
