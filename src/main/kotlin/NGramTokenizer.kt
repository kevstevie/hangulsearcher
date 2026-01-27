class NGramTokenizer {
    fun tokenize(text: String): List<String> {
        val result = mutableListOf<String>()
        val len = text.length

        val temp = StringBuilder()
        for (i in 0 until len) {
            for (j in i until len) {
                temp.append(text[j])
                result.add(temp.toString())
            }
            temp.clear()
        }

        return result
    }
}
