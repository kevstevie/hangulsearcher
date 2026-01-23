import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

class HangulTrieTest {

    @Test
    fun `문자열 저장 - 기본`() {
        val trie = HangulTrie()

        assertSoftly { softly ->
            softly.assertThat(trie.insert("안녕")).isTrue()
            softly.assertThat(trie.search("안녕")).isTrue()
            softly.assertThat(trie.size()).isEqualTo(1)
            softly.assertThat(trie.isEmpty()).isFalse()
        }
    }

    @Test
    fun `문자열 저장 - 여러 개`() {
        val trie = HangulTrie()

        assertSoftly { softly ->
            softly.assertThat(trie.insert("안녕")).isTrue()
            softly.assertThat(trie.insert("안녕하세요")).isTrue()
            softly.assertThat(trie.insert("안녕히가세요")).isTrue()
            softly.assertThat(trie.size()).isEqualTo(3)
        }
    }

    @Test
    fun `문자열 저장 - 중복 저장`() {
        val trie = HangulTrie()

        assertSoftly { softly ->
            softly.assertThat(trie.insert("안녕")).isTrue()
            softly.assertThat(trie.insert("안녕")).isTrue()
            softly.assertThat(trie.search("안녕")).isTrue()
            softly.assertThat(trie.size()).isEqualTo(1)
        }
    }

    @Test
    fun `문자열 저장 - 빈 문자열`() {
        val trie = HangulTrie()

        assertSoftly { softly ->
            softly.assertThat(trie.insert("")).isFalse()
            softly.assertThat(trie.size()).isEqualTo(0)
            softly.assertThat(trie.isEmpty()).isTrue()
        }
    }

    @Test
    fun `문자열 저장 - 한글이 아닌 문자만`() {
        val trie = HangulTrie()

        assertSoftly { softly ->
            softly.assertThat(trie.insert("Hello")).isTrue()
            softly.assertThat(trie.insert("123")).isTrue()
            softly.assertThat(trie.size()).isEqualTo(2)
        }
    }

    @Test
    fun `문자열 저장 - 한글과 영문 혼합`() {
        val trie = HangulTrie()

        assertSoftly { softly ->
            softly.assertThat(trie.insert("안녕Hello")).isTrue()
            softly.assertThat(trie.search("안녕Hello")).isTrue()
            softly.assertThat(trie.size()).isEqualTo(1)
        }
    }

    @Test
    fun `문자열 검색 - 존재하는 경우`() {
        val trie = HangulTrie()
        trie.insert("안녕")
        trie.insert("안녕하세요")

        assertSoftly { softly ->
            softly.assertThat(trie.search("안녕")).isTrue()
            softly.assertThat(trie.search("안녕하세요")).isTrue()
        }
    }

    @Test
    fun `문자열 검색 - 존재하지 않는 경우`() {
        val trie = HangulTrie()
        trie.insert("안녕")

        assertSoftly { softly ->
            softly.assertThat(trie.search("안녕하세요")).isFalse()
            softly.assertThat(trie.search("하세요")).isFalse()
            softly.assertThat(trie.search("")).isFalse()
        }
    }

    @Test
    fun `문자열 검색 - 접두사는 있지만 단어가 아닌 경우`() {
        val trie = HangulTrie()
        trie.insert("안녕하세요")

        assertSoftly { softly ->
            softly.assertThat(trie.search("안녕")).isFalse()
            softly.assertThat(trie.search("안녕하")).isFalse()
            softly.assertThat(trie.search("안녕하세요")).isTrue()
        }
    }

    @Test
    fun `문자열 삭제 - 기본`() {
        val trie = HangulTrie()
        trie.insert("안녕")

        assertSoftly { softly ->
            softly.assertThat(trie.delete("안녕")).isTrue()
            softly.assertThat(trie.search("안녕")).isFalse()
            softly.assertThat(trie.size()).isEqualTo(0)
            softly.assertThat(trie.isEmpty()).isTrue()
        }
    }

    @Test
    fun `문자열 삭제 - 여러 개 중 하나 삭제`() {
        val trie = HangulTrie()
        trie.insert("안녕")
        trie.insert("안녕하세요")
        trie.insert("안녕히가세요")

        assertSoftly { softly ->
            softly.assertThat(trie.delete("안녕")).isTrue()
            softly.assertThat(trie.search("안녕")).isFalse()
            softly.assertThat(trie.search("안녕하세요")).isTrue()
            softly.assertThat(trie.search("안녕히가세요")).isTrue()
            softly.assertThat(trie.size()).isEqualTo(2)
        }
    }

    @Test
    fun `문자열 삭제 - 존재하지 않는 단어`() {
        val trie = HangulTrie()
        trie.insert("안녕")

        assertSoftly { softly ->
            softly.assertThat(trie.delete("안녕하세요")).isFalse()
            softly.assertThat(trie.search("안녕")).isTrue()
            softly.assertThat(trie.size()).isEqualTo(1)
        }
    }

    @Test
    fun `문자열 삭제 - 빈 문자열`() {
        val trie = HangulTrie()
        trie.insert("안녕")

        assertSoftly { softly ->
            softly.assertThat(trie.delete("")).isFalse()
            softly.assertThat(trie.search("안녕")).isTrue()
            softly.assertThat(trie.size()).isEqualTo(1)
        }
    }

    @Test
    fun `문자열 삭제 - 중복 저장 후 삭제`() {
        val trie = HangulTrie()
        trie.insert("안녕")
        trie.insert("안녕")

        assertSoftly { softly ->
            softly.assertThat(trie.delete("안녕")).isTrue()
            softly.assertThat(trie.search("안녕")).isFalse()
            softly.assertThat(trie.size()).isEqualTo(0)
        }
    }

    @Test
    fun `접두사 검색 - startsWith`() {
        val trie = HangulTrie()
        trie.insert("안녕")
        trie.insert("안녕하세요")
        trie.insert("안녕히가세요")

        assertSoftly { softly ->
            softly.assertThat(trie.startsWith("안녕")).isTrue()
            softly.assertThat(trie.startsWith("안녕하")).isTrue()
            softly.assertThat(trie.startsWith("안녕하세요")).isTrue()
            softly.assertThat(trie.startsWith("하세요")).isFalse()
            softly.assertThat(trie.startsWith("녕하")).isFalse()
        }
    }

    @Test
    fun `접두사 검색 - 빈 문자열`() {
        val trie = HangulTrie()
        trie.insert("안녕")

        assertSoftly { softly ->
            softly.assertThat(trie.startsWith("")).isFalse()
        }
    }

    @Test
    fun `접두사로 시작하는 모든 문자열 검색`() {
        val trie = HangulTrie()
        trie.insert("안녕")
        trie.insert("안녕하세요")
        trie.insert("안녕히가세요")
        trie.insert("안녕하")

        val results = trie.searchByPrefix("안녕")

        assertSoftly { softly ->
            softly.assertThat(results.size).isEqualTo(4)
            softly.assertThat(results).contains("안녕")
            softly.assertThat(results).contains("안녕하세요")
            softly.assertThat(results).contains("안녕히가세요")
            softly.assertThat(results).contains("안녕하")
        }
    }

    @Test
    fun `접두사로 시작하는 모든 문자열 검색 - 부분 접두사`() {
        val trie = HangulTrie()
        trie.insert("안녕")
        trie.insert("안녕하세요")
        trie.insert("안녕히가세요")

        val results = trie.searchByPrefix("안녕하")

        assertSoftly { softly ->
            softly.assertThat(results.size).isEqualTo(1)
            softly.assertThat(results).contains("안녕하세요")
        }
    }

    @Test
    fun `접두사로 시작하는 모든 문자열 검색 - 존재하지 않는 접두사`() {
        val trie = HangulTrie()
        trie.insert("안녕")

        val results = trie.searchByPrefix("하세요")

        assertSoftly { softly ->
            softly.assertThat(results).isEmpty()
        }
    }

    @Test
    fun `접두사로 시작하는 모든 문자열 검색 - 빈 문자열`() {
        val trie = HangulTrie()
        trie.insert("안녕")

        val results = trie.searchByPrefix("")

        assertSoftly { softly ->
            softly.assertThat(results).isEmpty()
        }
    }

    @Test
    fun `부분 문자열 포함 여부 확인 - contains`() {
        val trie = HangulTrie()
        trie.insert("안녕하세요")
        trie.insert("반갑습니다")

        assertSoftly { softly ->
            softly.assertThat(trie.contains("안녕하세요")).isTrue()
            softly.assertThat(trie.contains("녕하")).isTrue()
            softly.assertThat(trie.contains("하세요")).isTrue()
            softly.assertThat(trie.contains("반갑")).isTrue()
            softly.assertThat(trie.contains("안녕히")).isFalse()
            softly.assertThat(trie.contains("없는단어")).isFalse()
        }
    }

    @Test
    fun `부분 문자열 포함 여부 확인 - 빈 문자열`() {
        val trie = HangulTrie()
        trie.insert("안녕")

        assertSoftly { softly ->
            softly.assertThat(trie.contains("")).isFalse()
        }
    }

    @Test
    fun `모든 단어 반환 - getAllWords`() {
        val trie = HangulTrie()
        trie.insert("안녕")
        trie.insert("안녕하세요")
        trie.insert("반갑습니다")

        val allWords = trie.getAllWords()

        assertSoftly { softly ->
            softly.assertThat(allWords.size).isEqualTo(3)
            softly.assertThat(allWords).contains("안녕")
            softly.assertThat(allWords).contains("안녕하세요")
            softly.assertThat(allWords).contains("반갑습니다")
        }
    }

    @Test
    fun `모든 단어 반환 - 빈 Trie`() {
        val trie = HangulTrie()

        val allWords = trie.getAllWords()

        assertSoftly { softly ->
            softly.assertThat(allWords).isEmpty()
        }
    }

    @Test
    fun `Trie 초기화 - clear`() {
        val trie = HangulTrie()
        trie.insert("안녕")
        trie.insert("안녕하세요")

        trie.clear()

        assertSoftly { softly ->
            softly.assertThat(trie.size()).isEqualTo(0)
            softly.assertThat(trie.isEmpty()).isTrue()
            softly.assertThat(trie.search("안녕")).isFalse()
            softly.assertThat(trie.search("안녕하세요")).isFalse()
            softly.assertThat(trie.getAllWords()).isEmpty()
        }
    }

    @Test
    fun `복합 시나리오 - 저장, 검색, 삭제, 접두사 검색`() {
        val trie = HangulTrie()

        assertSoftly { softly ->
            softly.assertThat(trie.insert("안녕")).isTrue()
            softly.assertThat(trie.insert("안녕하세요")).isTrue()
            softly.assertThat(trie.insert("안녕히가세요")).isTrue()
            softly.assertThat(trie.insert("반갑습니다")).isTrue()
            softly.assertThat(trie.size()).isEqualTo(4)
        }

        assertSoftly { softly ->
            softly.assertThat(trie.search("안녕")).isTrue()
            softly.assertThat(trie.search("안녕하세요")).isTrue()
            softly.assertThat(trie.search("안녕하")).isFalse()
        }

        val prefixResults = trie.searchByPrefix("안녕")
        assertSoftly { softly ->
            softly.assertThat(prefixResults.size).isEqualTo(3)
            softly.assertThat(prefixResults).contains("안녕")
            softly.assertThat(prefixResults).contains("안녕하세요")
            softly.assertThat(prefixResults).contains("안녕히가세요")
        }

        assertSoftly { softly ->
            softly.assertThat(trie.delete("안녕")).isTrue()
            softly.assertThat(trie.size()).isEqualTo(3)
            softly.assertThat(trie.search("안녕")).isFalse()
            softly.assertThat(trie.search("안녕하세요")).isTrue()
        }

        assertSoftly { softly ->
            softly.assertThat(trie.contains("녕하")).isTrue()
            softly.assertThat(trie.contains("반갑")).isTrue()
        }
    }

    @Test
    fun `한글과 영문 혼합 문자열 처리`() {
        val trie = HangulTrie()

        assertSoftly { softly ->
            softly.assertThat(trie.insert("안녕Hello")).isTrue()
            softly.assertThat(trie.insert("Hello안녕")).isTrue()
            softly.assertThat(trie.search("안녕Hello")).isTrue()
            softly.assertThat(trie.search("Hello안녕")).isTrue()
            softly.assertThat(trie.startsWith("안녕")).isTrue()
            softly.assertThat(trie.startsWith("Hello")).isTrue()
        }
    }

    @Test
    fun `받침 있는 단어와 없는 단어 혼합`() {
        val trie = HangulTrie()

        assertSoftly { softly ->
            softly.assertThat(trie.insert("가")).isTrue()
            softly.assertThat(trie.insert("각")).isTrue()
            softly.assertThat(trie.insert("간")).isTrue()
            softly.assertThat(trie.search("가")).isTrue()
            softly.assertThat(trie.search("각")).isTrue()
            softly.assertThat(trie.search("간")).isTrue()
            softly.assertThat(trie.size()).isEqualTo(3)
        }
    }

    @Test
    fun `동일한 접두사를 가진 여러 단어`() {
        val trie = HangulTrie()

        trie.insert("가나다")
        trie.insert("가나다라")
        trie.insert("가나다라마")
        trie.insert("가나")

        val results = trie.searchByPrefix("가나")

        assertSoftly { softly ->
            softly.assertThat(results.size).isEqualTo(4)
            softly.assertThat(results).contains("가나")
            softly.assertThat(results).contains("가나다")
            softly.assertThat(results).contains("가나다라")
            softly.assertThat(results).contains("가나다라마")
        }
    }

    @Test
    fun `삭제 후 접두사 검색`() {
        val trie = HangulTrie()
        trie.insert("안녕")
        trie.insert("안녕하세요")
        trie.insert("안녕히가세요")

        trie.delete("안녕하세요")

        val results = trie.searchByPrefix("안녕")

        assertSoftly { softly ->
            softly.assertThat(results.size).isEqualTo(2)
            softly.assertThat(results).contains("안녕")
            softly.assertThat(results).contains("안녕히가세요")
            softly.assertThat(results).doesNotContain("안녕하세요")
        }
    }

    @Test
    fun `빈 Trie에서의 모든 연산`() {
        val trie = HangulTrie()

        assertSoftly { softly ->
            softly.assertThat(trie.isEmpty()).isTrue()
            softly.assertThat(trie.size()).isEqualTo(0)
            softly.assertThat(trie.search("안녕")).isFalse()
            softly.assertThat(trie.delete("안녕")).isFalse()
            softly.assertThat(trie.startsWith("안녕")).isFalse()
            softly.assertThat(trie.searchByPrefix("안녕")).isEmpty()
            softly.assertThat(trie.getAllWords()).isEmpty()
        }
    }

    @Test
    fun `접두사 검색 특수 케이스`() {
        val trie = HangulTrie()

        trie.insert("가고")

        val results = trie.searchByPrefix("각")

        assertSoftly { softly ->
            softly.assertThat(results.size).isEqualTo(1)
        }
    }
}
