interface WordStorage {
    fun storeToken(token: String, word: String)
    fun getWordsByToken(token: String): Set<String>
    fun storeChosungToken(token: String, word: String)
    fun getWordsByChosungToken(token: String): Set<String>
    fun clear()
}
