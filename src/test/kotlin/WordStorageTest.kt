import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach

class WordStorageTest {

    private val wordStorage = WordStorage()

    @BeforeEach
    fun setup() {
        wordStorage.clear()
    }

    @Test
    fun `기본 저장 검색`() {
        wordStorage.storeWord("안녕하세요", "안녕히가세요", "안녕")
        val results = wordStorage.searchWords("안녕")

        assertSoftly { softly ->
            softly.assertThat(results).contains("안녕하세요")
            softly.assertThat(results).contains("안녕히가세요")
            softly.assertThat(results).contains("안녕")
        }
    }

    @Test
    fun `종성 분리 검색`() {
        wordStorage.storeWord("안녕히가세요")

        val results = wordStorage.searchWords("힉")

        assertSoftly { softly ->
            softly.assertThat(results).contains("안녕히가세요")
        }
    }

    @Test
    fun `기본 초성 검색`() {
        wordStorage.storeWord("안녕하세요", "안녕히가세요", "안녕")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWordsForChosung("ㅇㄴ")).hasSize(3)
            softly.assertThat(wordStorage.searchWordsForChosung("ㅇㄴㅎ")).hasSize(2)
        }
    }

    @Test
    fun `중복 저장`() {
        wordStorage.storeWord("안녕하세요", "안녕하세요", "안녕하세요")

        val results = wordStorage.searchWords("안녕하세요")

        assertThat(results).hasSize(1)
    }

    @Test
    fun `빈 문자열 저장`() {
        wordStorage.storeWord("")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("")).isEmpty()
            softly.assertThat(wordStorage.searchWordsForChosung("")).isEmpty()
        }
    }

    @Test
    fun `빈 문자열 검색`() {
        wordStorage.storeWord("안녕")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("")).isEmpty()
            softly.assertThat(wordStorage.searchWordsForChosung("")).isEmpty()
        }
    }

    @Test
    fun `한글이 아닌 문자만 저장`() {
        wordStorage.storeWord("Hello", "123", "!@#")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("Hello")).contains("Hello")
            softly.assertThat(wordStorage.searchWords("123")).contains("123")
            softly.assertThat(wordStorage.searchWords("!@#")).contains("!@#")
        }
    }

    @Test
    fun `한글과 영문 혼합 저장 검색`() {
        wordStorage.storeWord("안녕Hello", "Hello안녕", "안녕123")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("안녕Hello")).contains("안녕Hello")
            softly.assertThat(wordStorage.searchWords("Hello안녕")).contains("Hello안녕")
            softly.assertThat(wordStorage.searchWords("안녕123")).contains("안녕123")
        }
    }

    @Test
    fun `존재하지 않는 키워드 검색`() {
        wordStorage.storeWord("안녕하세요")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("없는단어")).isEmpty()
            softly.assertThat(wordStorage.searchWordsForChosung("없는초성")).isEmpty()
        }
    }

    @Test
    fun `부분 토큰 검색`() {
        wordStorage.storeWord("안녕하세요")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("안녕")).contains("안녕하세요")
            softly.assertThat(wordStorage.searchWords("녕하")).contains("안녕하세요")
            softly.assertThat(wordStorage.searchWords("하세요")).contains("안녕하세요")
        }
    }

    @Test
    fun `단일 문자 토큰 검색`() {
        wordStorage.storeWord("안녕")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("ㅇ")).contains("안녕")
            softly.assertThat(wordStorage.searchWords("ㅏ")).contains("안녕")
            softly.assertThat(wordStorage.searchWords("ㄴ")).contains("안녕")
        }
    }

    @Test
    fun `초성 검색 - 단일 초성`() {
        wordStorage.storeWord("안녕", "안녕하세요", "안녕히가세요")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWordsForChosung("ㅇ")).hasSize(3)
            softly.assertThat(wordStorage.searchWordsForChosung("ㄴ")).hasSize(3)
        }
    }

    @Test
    fun `초성 검색 - 부분 초성`() {
        wordStorage.storeWord("안녕하세요", "안녕히가세요")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWordsForChosung("ㅇㄴ")).hasSize(2)
            softly.assertThat(wordStorage.searchWordsForChosung("ㅇㄴㅎ")).hasSize(2)
            softly.assertThat(wordStorage.searchWordsForChosung("ㅇㄴㅎㅅ")).hasSize(1)
        }
    }

    @Test
    fun `초성 검색 - 전체 초성`() {
        wordStorage.storeWord("안녕하세요")

        val results = wordStorage.searchWordsForChosung("ㅇㄴㅎㅅㅇ")

        assertSoftly { softly ->
            softly.assertThat(results).contains("안녕하세요")
        }
    }

    @Test
    fun `초성 검색 - 한글이 아닌 문자 포함`() {
        wordStorage.storeWord("안녕Hello")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWordsForChosung("ㅇㄴ")).contains("안녕Hello")
            softly.assertThat(wordStorage.searchWordsForChosung("Hello")).contains("안녕Hello")
        }
    }

    @Test
    fun `여러 번 저장`() {
        wordStorage.storeWord("안녕")
        wordStorage.storeWord("안녕하세요")
        wordStorage.storeWord("안녕히가세요")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("안녕")).hasSize(3)
            softly.assertThat(wordStorage.searchWords("안녕하세요")).hasSize(1)
            softly.assertThat(wordStorage.searchWords("안녕히가세요")).hasSize(1)
        }
    }

    @Test
    fun `clear 후 검색`() {
        wordStorage.storeWord("안녕", "안녕하세요")
        wordStorage.clear()

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("안녕")).isEmpty()
            softly.assertThat(wordStorage.searchWords("안녕하세요")).isEmpty()
            softly.assertThat(wordStorage.searchWordsForChosung("ㅇㄴ")).isEmpty()
        }
    }

    @Test
    fun `clear 후 재저장`() {
        wordStorage.storeWord("안녕")
        wordStorage.clear()
        wordStorage.storeWord("반갑습니다")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("안녕")).isEmpty()
            softly.assertThat(wordStorage.searchWords("반갑습니다")).contains("반갑습니다")
        }
    }

    @Test
    fun `받침 있는 단어 검색`() {
        wordStorage.storeWord("값", "각", "간")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("값")).contains("값")
            softly.assertThat(wordStorage.searchWords("각")).contains("각")
            softly.assertThat(wordStorage.searchWords("간")).contains("간")
        }
    }

    @Test
    fun `복잡한 받침 검색`() {
        wordStorage.storeWord("값", "읊다", "읊어")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("값")).contains("값")
            softly.assertThat(wordStorage.searchWords("읊다")).contains("읊다")
            softly.assertThat(wordStorage.searchWords("읊어")).contains("읊어")
        }
    }

    @Test
    fun `긴 단어 검색`() {
        wordStorage.storeWord("안녕하세요반갑습니다")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("안녕")).contains("안녕하세요반갑습니다")
            softly.assertThat(wordStorage.searchWords("반갑")).contains("안녕하세요반갑습니다")
            softly.assertThat(wordStorage.searchWords("안녕하세요반갑습니다")).contains("안녕하세요반갑습니다")
        }
    }

    @Test
    fun `초성 검색 - 다양한 길이`() {
        wordStorage.storeWord("가나다라마")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWordsForChosung("ㄱ")).hasSize(1)
            softly.assertThat(wordStorage.searchWordsForChosung("ㄱㄴ")).hasSize(1)
            softly.assertThat(wordStorage.searchWordsForChosung("ㄱㄴㄷ")).hasSize(1)
            softly.assertThat(wordStorage.searchWordsForChosung("ㄱㄴㄷㄹ")).hasSize(1)
            softly.assertThat(wordStorage.searchWordsForChosung("ㄱㄴㄷㄹㅁ")).hasSize(1)
        }
    }

    @Test
    fun `특수문자 포함 단어`() {
        wordStorage.storeWord("안녕!", "안녕?", "안녕.")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("안녕!")).contains("안녕!")
            softly.assertThat(wordStorage.searchWords("안녕?")).contains("안녕?")
            softly.assertThat(wordStorage.searchWords("안녕.")).contains("안녕.")
        }
    }

    @Test
    fun `숫자 포함 단어`() {
        wordStorage.storeWord("안녕123", "123안녕", "안123녕")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWords("안녕123")).contains("안녕123")
            softly.assertThat(wordStorage.searchWords("123안녕")).contains("123안녕")
            softly.assertThat(wordStorage.searchWords("안123녕")).contains("안123녕")
        }
    }

    @kotlin.test.Test
    fun `한글이 아닌 단어를 포함한 초성 검색`() {
        wordStorage.storeWord("Hello안녕", "123안녕", "!@#안녕")

        assertSoftly { softly ->
            softly.assertThat(wordStorage.searchWordsForChosung("Helloㅇㄴ")).contains("Hello안녕")
            softly.assertThat(wordStorage.searchWordsForChosung("123ㅇㄴ")).contains("123안녕")
            softly.assertThat(wordStorage.searchWordsForChosung("!@#ㅇㄴ")).contains("!@#안녕")
        }
    }
}
