interface WordStorage {
    fun storeWordWithTokens(tokens: List<String>, word: String)
    fun getWordsByToken(token: String): Set<String>
    fun storeWordWithChosungTokens(tokens: List<String>, word: String)
    fun getWordsByChosungToken(token: String): Set<String>
    fun clear()
}
