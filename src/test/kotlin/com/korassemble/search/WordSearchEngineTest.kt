package com.korassemble.search

import com.korassemble.storage.MemoryWordStorage
import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach

class WordSearchEngineTest {

    private val searchEngine = WordSearchEngine()

    @BeforeEach
    fun setup() {
        searchEngine.clear()
    }

    @Test
    fun `기본 저장 검색`() {
        searchEngine.storeWords("안녕하세요", "안녕히가세요", "안녕")
        val results = searchEngine.searchWords("안녕")

        assertSoftly { softly ->
            softly.assertThat(results).contains("안녕하세요")
            softly.assertThat(results).contains("안녕히가세요")
            softly.assertThat(results).contains("안녕")
        }
    }

    @Test
    fun `종성 분리 검색`() {
        searchEngine.storeWords("안녕히가세요")

        val results = searchEngine.searchWords("힉")

        assertSoftly { softly ->
            softly.assertThat(results).contains("안녕히가세요")
        }
    }

    @Test
    fun `기본 초성 검색`() {
        searchEngine.storeWords("안녕하세요", "안녕히가세요", "안녕")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWordsForChosung("ㅇㄴ")).hasSize(3)
            softly.assertThat(searchEngine.searchWordsForChosung("ㅇㄴㅎ")).hasSize(2)
        }
    }

    @Test
    fun `중복 저장`() {
        searchEngine.storeWords("안녕하세요", "안녕하세요", "안녕하세요")

        val results = searchEngine.searchWords("안녕하세요")

        assertThat(results).hasSize(1)
    }

    @Test
    fun `빈 문자열 저장`() {
        searchEngine.storeWords("")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("")).isEmpty()
            softly.assertThat(searchEngine.searchWordsForChosung("")).isEmpty()
        }
    }

    @Test
    fun `빈 문자열 검색`() {
        searchEngine.storeWords("안녕")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("")).isEmpty()
            softly.assertThat(searchEngine.searchWordsForChosung("")).isEmpty()
        }
    }

    @Test
    fun `한글이 아닌 문자만 저장`() {
        searchEngine.storeWords("Hello", "123", "!@#")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("Hello")).contains("Hello")
            softly.assertThat(searchEngine.searchWords("123")).contains("123")
            softly.assertThat(searchEngine.searchWords("!@#")).contains("!@#")
        }
    }

    @Test
    fun `한글과 영문 혼합 저장 검색`() {
        searchEngine.storeWords("안녕Hello", "Hello안녕", "안녕123")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("안녕Hello")).contains("안녕Hello")
            softly.assertThat(searchEngine.searchWords("Hello안녕")).contains("Hello안녕")
            softly.assertThat(searchEngine.searchWords("안녕123")).contains("안녕123")
        }
    }

    @Test
    fun `존재하지 않는 키워드 검색`() {
        searchEngine.storeWords("안녕하세요")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("없는단어")).isEmpty()
            softly.assertThat(searchEngine.searchWordsForChosung("없는초성")).isEmpty()
        }
    }

    @Test
    fun `부분 토큰 검색`() {
        searchEngine.storeWords("안녕하세요")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("안녕")).contains("안녕하세요")
            softly.assertThat(searchEngine.searchWords("녕하")).contains("안녕하세요")
            softly.assertThat(searchEngine.searchWords("하세요")).contains("안녕하세요")
        }
    }

    @Test
    fun `단일 문자 토큰 검색`() {
        searchEngine.storeWords("안녕")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("ㅇ")).contains("안녕")
            softly.assertThat(searchEngine.searchWords("ㅏ")).contains("안녕")
            softly.assertThat(searchEngine.searchWords("ㄴ")).contains("안녕")
        }
    }

    @Test
    fun `초성 검색 - 단일 초성`() {
        searchEngine.storeWords("안녕", "안녕하세요", "안녕히가세요")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWordsForChosung("ㅇ")).hasSize(3)
            softly.assertThat(searchEngine.searchWordsForChosung("ㄴ")).hasSize(3)
        }
    }

    @Test
    fun `초성 검색 - 부분 초성`() {
        searchEngine.storeWords("안녕하세요", "안녕히가세요")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWordsForChosung("ㅇㄴ")).hasSize(2)
            softly.assertThat(searchEngine.searchWordsForChosung("ㅇㄴㅎ")).hasSize(2)
            softly.assertThat(searchEngine.searchWordsForChosung("ㅇㄴㅎㅅ")).hasSize(1)
        }
    }

    @Test
    fun `초성 검색 - 전체 초성`() {
        searchEngine.storeWords("안녕하세요")

        val results = searchEngine.searchWordsForChosung("ㅇㄴㅎㅅㅇ")

        assertSoftly { softly ->
            softly.assertThat(results).contains("안녕하세요")
        }
    }

    @Test
    fun `초성 검색 - 한글이 아닌 문자 포함`() {
        searchEngine.storeWords("안녕Hello")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWordsForChosung("ㅇㄴ")).contains("안녕Hello")
            softly.assertThat(searchEngine.searchWordsForChosung("Hello")).contains("안녕Hello")
        }
    }

    @Test
    fun `여러 번 저장`() {
        searchEngine.storeWords("안녕")
        searchEngine.storeWords("안녕하세요")
        searchEngine.storeWords("안녕히가세요")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("안녕")).hasSize(3)
            softly.assertThat(searchEngine.searchWords("안녕하세요")).hasSize(1)
            softly.assertThat(searchEngine.searchWords("안녕히가세요")).hasSize(1)
        }
    }

    @Test
    fun `clear 후 검색`() {
        searchEngine.storeWords("안녕", "안녕하세요")
        searchEngine.clear()

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("안녕")).isEmpty()
            softly.assertThat(searchEngine.searchWords("안녕하세요")).isEmpty()
            softly.assertThat(searchEngine.searchWordsForChosung("ㅇㄴ")).isEmpty()
        }
    }

    @Test
    fun `clear 후 재저장`() {
        searchEngine.storeWords("안녕")
        searchEngine.clear()
        searchEngine.storeWords("반갑습니다")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("안녕")).isEmpty()
            softly.assertThat(searchEngine.searchWords("반갑습니다")).contains("반갑습니다")
        }
    }

    @Test
    fun `받침 있는 단어 검색`() {
        searchEngine.storeWords("값", "각", "간")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("값")).contains("값")
            softly.assertThat(searchEngine.searchWords("각")).contains("각")
            softly.assertThat(searchEngine.searchWords("간")).contains("간")
        }
    }

    @Test
    fun `복잡한 받침 검색`() {
        searchEngine.storeWords("값", "읊다", "읊어")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("값")).contains("값")
            softly.assertThat(searchEngine.searchWords("읊다")).contains("읊다")
            softly.assertThat(searchEngine.searchWords("읊어")).contains("읊어")
        }
    }

    @Test
    fun `긴 단어 검색`() {
        searchEngine.storeWords("안녕하세요반갑습니다")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("안녕")).contains("안녕하세요반갑습니다")
            softly.assertThat(searchEngine.searchWords("반갑")).contains("안녕하세요반갑습니다")
            softly.assertThat(searchEngine.searchWords("안녕하세요반갑습니다")).contains("안녕하세요반갑습니다")
        }
    }

    @Test
    fun `초성 검색 - 다양한 길이`() {
        searchEngine.storeWords("가나다라마")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWordsForChosung("ㄱ")).hasSize(1)
            softly.assertThat(searchEngine.searchWordsForChosung("ㄱㄴ")).hasSize(1)
            softly.assertThat(searchEngine.searchWordsForChosung("ㄱㄴㄷ")).hasSize(1)
            softly.assertThat(searchEngine.searchWordsForChosung("ㄱㄴㄷㄹ")).hasSize(1)
            softly.assertThat(searchEngine.searchWordsForChosung("ㄱㄴㄷㄹㅁ")).hasSize(1)
        }
    }

    @Test
    fun `특수문자 포함 단어`() {
        searchEngine.storeWords("안녕!", "안녕?", "안녕.")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("안녕!")).contains("안녕!")
            softly.assertThat(searchEngine.searchWords("안녕?")).contains("안녕?")
            softly.assertThat(searchEngine.searchWords("안녕.")).contains("안녕.")
        }
    }

    @Test
    fun `숫자 포함 단어`() {
        searchEngine.storeWords("안녕123", "123안녕", "안123녕")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWords("안녕123")).contains("안녕123")
            softly.assertThat(searchEngine.searchWords("123안녕")).contains("123안녕")
            softly.assertThat(searchEngine.searchWords("안123녕")).contains("안123녕")
        }
    }

    @Test
    fun `한글이 아닌 단어를 포함한 초성 검색`() {
        searchEngine.storeWords("Hello안녕", "123안녕", "!@#안녕")

        assertSoftly { softly ->
            softly.assertThat(searchEngine.searchWordsForChosung("Helloㅇㄴ")).contains("Hello안녕")
            softly.assertThat(searchEngine.searchWordsForChosung("123ㅇㄴ")).contains("123안녕")
            softly.assertThat(searchEngine.searchWordsForChosung("!@#ㅇㄴ")).contains("!@#안녕")
        }
    }

    @Test
    fun `커스텀 스토리지 사용`() {
        val customStorage = MemoryWordStorage()
        val customEngine = WordSearchEngine(storage = customStorage)

        customEngine.storeWords("테스트")
        val results = customEngine.searchWords("테스트")

        assertThat(results).contains("테스트")
    }

    @Test
    fun `된발음 분리 검색`() {
        searchEngine.storeWords("까")

        val result = searchEngine.searchWords("ㄲ")

        assertThat(result).contains("까")
    }
}