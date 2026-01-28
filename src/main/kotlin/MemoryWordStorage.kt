class MemoryWordStorage : WordStorage {

    private val tokenToWords = mutableMapOf<String, MutableSet<String>>()
    private val chosungTokenToWords = mutableMapOf<String, MutableSet<String>>()

    override fun saveWordWithTokens(word: TokenizedWord) {
        for (token in word.tokens) {
            val wordSet = tokenToWords.getOrPut(token) { mutableSetOf() }
            wordSet.add(word.original)
        }
    }

    override fun saveAllWordWithTokens(words: List<TokenizedWord>) {
        for (word in words) {
            saveWordWithTokens(word)
        }
    }

    override fun getWordsByToken(token: String): Set<String> {
        return tokenToWords[token] ?: emptySet()
    }

    override fun saveWordWithChosungTokens(word: TokenizedWord) {
        for (token in word.tokens) {
            val wordSet = chosungTokenToWords.getOrPut(token) { mutableSetOf() }
            wordSet.add(word.original)
        }
    }

    override fun saveAllWordWithChosungTokens(words: List<TokenizedWord>) {
        for (word in words) {
            saveWordWithChosungTokens(word)
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
