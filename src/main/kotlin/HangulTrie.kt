class HangulTrie(private val decomposer: HangulDecomposer = HangulDecomposer()) {

    private val root = TrieNode()
    private var size = 0

    private class TrieNode {
        val children = mutableMapOf<Char, TrieNode>()
        var isEndOfWord = false
        var originalWord: String? = null
    }

    fun insert(word: String): Boolean {
        if (word.isEmpty()) return false

        val decomposed = decomposer.decomposeToString(word)
        if (decomposed.isEmpty()) return false

        var current = root

        for (char in decomposed) {
            current.children.getOrPut(char) { TrieNode() }
            current = current.children[char]!!
        }

        if (!current.isEndOfWord) {
            current.isEndOfWord = true
            size++
        }
        current.originalWord = word

        return true
    }

    fun delete(word: String): Boolean {
        if (word.isEmpty()) return false

        val decomposed = decomposer.decomposeToString(word)
        if (decomposed.isEmpty()) return false

        var current = root

        for (char in decomposed) {
            current = current.children[char] ?: return false
        }

        if (current.originalWord != word) {
            return false
        }

        current.originalWord = null
        current.isEndOfWord = false
        size--

        return true
    }

    fun search(word: String): Boolean {
        if (word.isEmpty()) return false

        val decomposed = decomposer.decomposeToString(word)
        if (decomposed.isEmpty()) return false

        var current = root

        for (char in decomposed) {
            val child = current.children[char] ?: return false
            current = child
        }

        return current.isEndOfWord
    }

    fun startsWith(prefix: String): Boolean {
        if (prefix.isEmpty()) return false

        val decomposed = decomposer.decomposeToString(prefix)
        if (decomposed.isEmpty()) return false

        var current = root

        for (char in decomposed) {
            val child = current.children[char] ?: return false
            current = child
        }

        return true
    }

    fun searchByPrefix(prefix: String): List<String> {
        if (prefix.isEmpty()) return emptyList()

        val decomposed = decomposer.decomposeToString(prefix)
        if (decomposed.isEmpty()) return emptyList()

        var current = root

        for (char in decomposed) {
            val child = current.children[char] ?: return emptyList()
            current = child
        }

        val results = mutableSetOf<String>()
        collectWords(current, results)

        return results.toList()
    }

    private fun collectWords(node: TrieNode, results: MutableSet<String>) {
        node.originalWord?.let { results.add(it) }

        for (child in node.children.values) {
            collectWords(child, results)
        }
    }

    fun size(): Int = size

    fun isEmpty(): Boolean = size == 0

    fun getAllWords(): List<String> {
        val results = mutableSetOf<String>()
        collectWords(root, results)
        return results.toList()
    }

    fun clear() {
        root.children.clear()
        root.originalWord = null
        root.isEndOfWord = false
        size = 0
    }

    fun contains(word: String): Boolean {
        if (word.isEmpty()) return false

        val decomposed = decomposer.decomposeToString(word)
        if (decomposed.isEmpty()) return false

        val allWords = getAllWords()
        for (storedWord in allWords) {
            val storedDecomposed = decomposer.decomposeToString(storedWord)
            if (storedDecomposed.contains(decomposed)) {
                return true
            }
        }

        return false
    }
}
