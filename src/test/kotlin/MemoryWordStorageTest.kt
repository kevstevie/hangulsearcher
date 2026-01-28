import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach

class MemoryWordStorageTest {

    private val storage = MemoryWordStorage()

    @BeforeEach
    fun setup() {
        storage.clear()
    }

    // saveWordWithTokens 테스트

    @Test
    fun `saveWordWithTokens - 단어 저장 후 토큰으로 검색`() {
        val word = TokenizedWord("안녕", listOf("안", "안녕", "녕"))

        storage.saveWordWithTokens(word)

        assertSoftly { softly ->
            softly.assertThat(storage.getWordsByToken("안")).containsExactly("안녕")
            softly.assertThat(storage.getWordsByToken("안녕")).containsExactly("안녕")
            softly.assertThat(storage.getWordsByToken("녕")).containsExactly("안녕")
        }
    }

    @Test
    fun `saveWordWithTokens - 여러 단어 개별 저장`() {
        storage.saveWordWithTokens(TokenizedWord("안녕", listOf("안", "안녕")))
        storage.saveWordWithTokens(TokenizedWord("안녕하세요", listOf("안", "안녕")))

        assertThat(storage.getWordsByToken("안녕")).containsExactlyInAnyOrder("안녕", "안녕하세요")
    }

    @Test
    fun `saveWordWithTokens - 빈 토큰 리스트`() {
        val word = TokenizedWord("테스트", emptyList())

        storage.saveWordWithTokens(word)

        assertThat(storage.getWordsByToken("테스트")).isEmpty()
    }

    @Test
    fun `saveWordWithTokens - 동일 단어 중복 저장시 중복 제거`() {
        val word = TokenizedWord("테스트", listOf("테", "테스"))

        storage.saveWordWithTokens(word)
        storage.saveWordWithTokens(word)

        assertThat(storage.getWordsByToken("테")).hasSize(1)
    }

    // saveAllWordWithTokens 테스트

    @Test
    fun `saveAllWordWithTokens - 여러 단어 일괄 저장`() {
        val words = listOf(
            TokenizedWord("안녕", listOf("안", "안녕", "녕")),
            TokenizedWord("안녕하세요", listOf("안", "안녕", "녕하", "하세", "세요"))
        )

        storage.saveAllWordWithTokens(words)

        assertSoftly { softly ->
            softly.assertThat(storage.getWordsByToken("안녕")).containsExactlyInAnyOrder("안녕", "안녕하세요")
            softly.assertThat(storage.getWordsByToken("안")).containsExactlyInAnyOrder("안녕", "안녕하세요")
            softly.assertThat(storage.getWordsByToken("세요")).containsExactly("안녕하세요")
        }
    }

    @Test
    fun `saveAllWordWithTokens - 빈 리스트`() {
        storage.saveAllWordWithTokens(emptyList())

        assertThat(storage.getWordsByToken("any")).isEmpty()
    }

    @Test
    fun `saveAllWordWithTokens - 단일 요소`() {
        val words = listOf(TokenizedWord("테스트", listOf("테", "테스", "스트")))

        storage.saveAllWordWithTokens(words)

        assertSoftly { softly ->
            softly.assertThat(storage.getWordsByToken("테스")).containsExactly("테스트")
            softly.assertThat(storage.getWordsByToken("스트")).containsExactly("테스트")
        }
    }

    @Test
    fun `saveAllWordWithTokens - 중복 토큰이 있는 단어들`() {
        val words = listOf(
            TokenizedWord("가나", listOf("가", "가나", "나")),
            TokenizedWord("가다", listOf("가", "가다", "다"))
        )

        storage.saveAllWordWithTokens(words)

        assertSoftly { softly ->
            softly.assertThat(storage.getWordsByToken("가")).containsExactlyInAnyOrder("가나", "가다")
            softly.assertThat(storage.getWordsByToken("가나")).containsExactly("가나")
            softly.assertThat(storage.getWordsByToken("가다")).containsExactly("가다")
        }
    }

    @Test
    fun `saveAllWordWithTokens - 동일 단어 중복 저장시 중복 제거`() {
        val words = listOf(
            TokenizedWord("테스트", listOf("테", "테스", "스트")),
            TokenizedWord("테스트", listOf("테", "테스", "스트"))
        )

        storage.saveAllWordWithTokens(words)

        assertThat(storage.getWordsByToken("테스")).hasSize(1)
    }

    // getWordsByToken 테스트

    @Test
    fun `getWordsByToken - 존재하지 않는 토큰 검색`() {
        storage.saveWordWithTokens(TokenizedWord("안녕", listOf("안녕")))

        assertThat(storage.getWordsByToken("없는토큰")).isEmpty()
    }

    @Test
    fun `getWordsByToken - 빈 저장소에서 검색`() {
        assertThat(storage.getWordsByToken("any")).isEmpty()
    }

    // saveWordWithChosungTokens 테스트

    @Test
    fun `saveWordWithChosungTokens - 단어 저장 후 초성 토큰으로 검색`() {
        val word = TokenizedWord("안녕", listOf("ㅇ", "ㅇㄴ", "ㄴ"))

        storage.saveWordWithChosungTokens(word)

        assertSoftly { softly ->
            softly.assertThat(storage.getWordsByChosungToken("ㅇ")).containsExactly("안녕")
            softly.assertThat(storage.getWordsByChosungToken("ㅇㄴ")).containsExactly("안녕")
            softly.assertThat(storage.getWordsByChosungToken("ㄴ")).containsExactly("안녕")
        }
    }

    @Test
    fun `saveWordWithChosungTokens - 여러 단어 개별 저장`() {
        storage.saveWordWithChosungTokens(TokenizedWord("안녕", listOf("ㅇ", "ㅇㄴ")))
        storage.saveWordWithChosungTokens(TokenizedWord("안녕하세요", listOf("ㅇ", "ㅇㄴ")))

        assertThat(storage.getWordsByChosungToken("ㅇㄴ")).containsExactlyInAnyOrder("안녕", "안녕하세요")
    }

    @Test
    fun `saveWordWithChosungTokens - 빈 토큰 리스트`() {
        val word = TokenizedWord("테스트", emptyList())

        storage.saveWordWithChosungTokens(word)

        assertThat(storage.getWordsByChosungToken("ㅌㅅㅌ")).isEmpty()
    }

    @Test
    fun `saveWordWithChosungTokens - 동일 단어 중복 저장시 중복 제거`() {
        val word = TokenizedWord("테스트", listOf("ㅌ", "ㅌㅅ"))

        storage.saveWordWithChosungTokens(word)
        storage.saveWordWithChosungTokens(word)

        assertThat(storage.getWordsByChosungToken("ㅌ")).hasSize(1)
    }

    // saveAllWordWithChosungTokens 테스트

    @Test
    fun `saveAllWordWithChosungTokens - 여러 단어 일괄 저장`() {
        val words = listOf(
            TokenizedWord("안녕", listOf("ㅇ", "ㅇㄴ", "ㄴ")),
            TokenizedWord("안녕하세요", listOf("ㅇ", "ㅇㄴ", "ㄴㅎ", "ㅎㅅ", "ㅅㅇ"))
        )

        storage.saveAllWordWithChosungTokens(words)

        assertSoftly { softly ->
            softly.assertThat(storage.getWordsByChosungToken("ㅇㄴ")).containsExactlyInAnyOrder("안녕", "안녕하세요")
            softly.assertThat(storage.getWordsByChosungToken("ㅇ")).containsExactlyInAnyOrder("안녕", "안녕하세요")
            softly.assertThat(storage.getWordsByChosungToken("ㅅㅇ")).containsExactly("안녕하세요")
        }
    }

    @Test
    fun `saveAllWordWithChosungTokens - 빈 리스트`() {
        storage.saveAllWordWithChosungTokens(emptyList())

        assertThat(storage.getWordsByChosungToken("any")).isEmpty()
    }

    @Test
    fun `saveAllWordWithChosungTokens - 단일 요소`() {
        val words = listOf(TokenizedWord("가나다", listOf("ㄱ", "ㄱㄴ", "ㄴㄷ")))

        storage.saveAllWordWithChosungTokens(words)

        assertSoftly { softly ->
            softly.assertThat(storage.getWordsByChosungToken("ㄱㄴ")).containsExactly("가나다")
            softly.assertThat(storage.getWordsByChosungToken("ㄴㄷ")).containsExactly("가나다")
        }
    }

    @Test
    fun `saveAllWordWithChosungTokens - 중복 토큰이 있는 단어들`() {
        val words = listOf(
            TokenizedWord("가나", listOf("ㄱ", "ㄱㄴ", "ㄴ")),
            TokenizedWord("가다", listOf("ㄱ", "ㄱㄷ", "ㄷ"))
        )

        storage.saveAllWordWithChosungTokens(words)

        assertSoftly { softly ->
            softly.assertThat(storage.getWordsByChosungToken("ㄱ")).containsExactlyInAnyOrder("가나", "가다")
            softly.assertThat(storage.getWordsByChosungToken("ㄱㄴ")).containsExactly("가나")
            softly.assertThat(storage.getWordsByChosungToken("ㄱㄷ")).containsExactly("가다")
        }
    }

    @Test
    fun `saveAllWordWithChosungTokens - 동일 단어 중복 저장시 중복 제거`() {
        val words = listOf(
            TokenizedWord("테스트", listOf("ㅌ", "ㅌㅅ", "ㅅㅌ")),
            TokenizedWord("테스트", listOf("ㅌ", "ㅌㅅ", "ㅅㅌ"))
        )

        storage.saveAllWordWithChosungTokens(words)

        assertThat(storage.getWordsByChosungToken("ㅌㅅ")).hasSize(1)
    }

    // getWordsByChosungToken 테스트

    @Test
    fun `getWordsByChosungToken - 존재하지 않는 토큰 검색`() {
        storage.saveWordWithChosungTokens(TokenizedWord("안녕", listOf("ㅇㄴ")))

        assertThat(storage.getWordsByChosungToken("ㅎㅎ")).isEmpty()
    }

    @Test
    fun `getWordsByChosungToken - 빈 저장소에서 검색`() {
        assertThat(storage.getWordsByChosungToken("any")).isEmpty()
    }

    // clear 테스트

    @Test
    fun `clear - 토큰 저장소 초기화`() {
        storage.saveWordWithTokens(TokenizedWord("안녕", listOf("안녕")))
        storage.saveWordWithChosungTokens(TokenizedWord("안녕", listOf("ㅇㄴ")))

        storage.clear()

        assertSoftly { softly ->
            softly.assertThat(storage.getWordsByToken("안녕")).isEmpty()
            softly.assertThat(storage.getWordsByChosungToken("ㅇㄴ")).isEmpty()
        }
    }

    @Test
    fun `clear 후 재저장`() {
        storage.saveWordWithTokens(TokenizedWord("안녕", listOf("안녕")))
        storage.clear()
        storage.saveWordWithTokens(TokenizedWord("반갑", listOf("반갑")))

        assertSoftly { softly ->
            softly.assertThat(storage.getWordsByToken("안녕")).isEmpty()
            softly.assertThat(storage.getWordsByToken("반갑")).containsExactly("반갑")
        }
    }

    // 토큰 저장소와 초성 저장소 독립성 테스트

    @Test
    fun `토큰 저장소와 초성 저장소는 독립적`() {
        storage.saveWordWithTokens(TokenizedWord("안녕", listOf("토큰1")))
        storage.saveWordWithChosungTokens(TokenizedWord("안녕", listOf("초성1")))

        assertSoftly { softly ->
            softly.assertThat(storage.getWordsByToken("토큰1")).containsExactly("안녕")
            softly.assertThat(storage.getWordsByToken("초성1")).isEmpty()
            softly.assertThat(storage.getWordsByChosungToken("초성1")).containsExactly("안녕")
            softly.assertThat(storage.getWordsByChosungToken("토큰1")).isEmpty()
        }
    }
}