import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HangulDecomposerTest {

    private val decomposer = HangulDecomposer()

    @Test
    fun `한글 문자 분리 - 받침 있는 경우`() {
        val result = decomposer.decompose('한')

        assertSoftly { softly ->
            softly.assertThat(result.original).isEqualTo('한')
            softly.assertThat(result.chosung).isEqualTo('ㅎ')
            softly.assertThat(result.jungsung).isEqualTo('ㅏ')
            softly.assertThat(result.jongsung).isEqualTo('ㄴ')
        }
    }

    @Test
    fun `한글 문자 분리 - 받침 없는 경우`() {
        val result = decomposer.decompose('가')

        assertSoftly { softly ->
            softly.assertThat(result.original).isEqualTo('가')
            softly.assertThat(result.chosung).isEqualTo('ㄱ')
            softly.assertThat(result.jungsung).isEqualTo('ㅏ')
            softly.assertThat(result.jongsung).isNull()
        }
    }

    @Test
    fun `한글 문자 분리 - 복잡한 받침`() {
        val result = decomposer.decompose('값')

        assertSoftly { softly ->
            softly.assertThat(result.original).isEqualTo('값')
            softly.assertThat(result.chosung).isEqualTo('ㄱ')
            softly.assertThat(result.jungsung).isEqualTo('ㅏ')
            softly.assertThat(result.jongsung).isEqualTo('ㅄ')
        }
    }

    @Test
    fun `한글 문자 분리 - 경계값 테스트 - 가`() {
        val result = decomposer.decompose('가')

        assertSoftly { softly ->
            softly.assertThat(result.original).isEqualTo('가')
            softly.assertThat(result.chosung).isEqualTo('ㄱ')
            softly.assertThat(result.jungsung).isEqualTo('ㅏ')
            softly.assertThat(result.jongsung).isNull()
        }
    }

    @Test
    fun `한글 문자 분리 - 경계값 테스트 - 힣`() {
        val result = decomposer.decompose('힣')

        assertSoftly { softly ->
            softly.assertThat(result.original).isEqualTo('힣')
            softly.assertThat(result.chosung).isEqualTo('ㅎ')
            softly.assertThat(result.jungsung).isEqualTo('ㅣ')
            softly.assertThat(result.jongsung).isEqualTo('ㅎ')
        }
    }

    @Test
    fun `한글이 아닌 문자 분리 - 영문`() {
        val result = decomposer.decompose('A')

        assertSoftly { softly ->
            softly.assertThat(result.original).isEqualTo('A')
            softly.assertThat(result.chosung).isNull()
            softly.assertThat(result.jungsung).isNull()
            softly.assertThat(result.jongsung).isNull()
        }
    }

    @Test
    fun `한글이 아닌 문자 분리 - 숫자`() {
        val result = decomposer.decompose('1')

        assertSoftly { softly ->
            softly.assertThat(result.original).isEqualTo('1')
            softly.assertThat(result.chosung).isNull()
            softly.assertThat(result.jungsung).isNull()
            softly.assertThat(result.jongsung).isNull()
        }
    }

    @Test
    fun `한글이 아닌 문자 분리 - 특수문자`() {
        val result = decomposer.decompose('!')

        assertSoftly { softly ->
            softly.assertThat(result.original).isEqualTo('!')
            softly.assertThat(result.chosung).isNull()
            softly.assertThat(result.jungsung).isNull()
            softly.assertThat(result.jongsung).isNull()
        }
    }

    @Test
    fun `문자열 전체 분리`() {
        val result = decomposer.decomposeAll("안녕하세요")

        assertSoftly { softly ->
            softly.assertThat(result.size).isEqualTo(5)
            softly.assertThat(result[0].original).isEqualTo('안')
            softly.assertThat(result[0].chosung).isEqualTo('ㅇ')
            softly.assertThat(result[0].jungsung).isEqualTo('ㅏ')
            softly.assertThat(result[0].jongsung).isEqualTo('ㄴ')
            softly.assertThat(result[1].original).isEqualTo('녕')
            softly.assertThat(result[1].chosung).isEqualTo('ㄴ')
            softly.assertThat(result[1].jungsung).isEqualTo('ㅕ')
            softly.assertThat(result[1].jongsung).isEqualTo('ㅇ')
            softly.assertThat(result[2].original).isEqualTo('하')
            softly.assertThat(result[2].chosung).isEqualTo('ㅎ')
            softly.assertThat(result[2].jungsung).isEqualTo('ㅏ')
            softly.assertThat(result[2].jongsung).isNull()
            softly.assertThat(result[3].original).isEqualTo('세')
            softly.assertThat(result[3].chosung).isEqualTo('ㅅ')
            softly.assertThat(result[3].jungsung).isEqualTo('ㅔ')
            softly.assertThat(result[3].jongsung).isNull()
            softly.assertThat(result[4].original).isEqualTo('요')
            softly.assertThat(result[4].chosung).isEqualTo('ㅇ')
            softly.assertThat(result[4].jungsung).isEqualTo('ㅛ')
            softly.assertThat(result[4].jongsung).isNull()
        }
    }

    @Test
    fun `문자열 전체 분리 - 한글과 영문 혼합`() {
        val result = decomposer.decomposeAll("Hello안녕")

        assertSoftly { softly ->
            softly.assertThat(result.size).isEqualTo(7)
            softly.assertThat(result[0].original).isEqualTo('H')
            softly.assertThat(result[0].chosung).isNull()
            softly.assertThat(result[5].original).isEqualTo('안')
            softly.assertThat(result[5].chosung).isEqualTo('ㅇ')
            softly.assertThat(result[5].jungsung).isEqualTo('ㅏ')
            softly.assertThat(result[5].jongsung).isEqualTo('ㄴ')
        }
    }

    @Test
    fun `문자열로 변환 - 한글만`() {
        val result = decomposer.decomposeToString("안녕")

        assertEquals("ㅇㅏㄴㄴㅕㅇ", result)
    }

    @Test
    fun `문자열로 변환 - 한글과 영문 혼합`() {
        val result = decomposer.decomposeToString("Hello안녕")

        assertEquals("Helloㅇㅏㄴㄴㅕㅇ", result)
    }

    @Test
    fun `문자열로 변환 - 받침 없는 경우`() {
        val result = decomposer.decomposeToString("가나다")

        assertEquals("ㄱㅏㄴㅏㄷㅏ", result)
    }

    @Test
    fun `문자열로 변환 - 숫자와 특수문자 포함`() {
        val result = decomposer.decomposeToString("안녕123!")

        assertEquals("ㅇㅏㄴㄴㅕㅇ123!", result)
    }

    @Test
    fun `초성 추출`() {
        assertSoftly { softly ->
            softly.assertThat(decomposer.getInitial('한')).isEqualTo('ㅎ')
            softly.assertThat(decomposer.getInitial('가')).isEqualTo('ㄱ')
            softly.assertThat(decomposer.getInitial('녕')).isEqualTo('ㄴ')
        }
    }

    @Test
    fun `초성 추출 - 한글이 아닌 경우`() {
        assertSoftly { softly ->
            softly.assertThat(decomposer.getInitial('A')).isNull()
            softly.assertThat(decomposer.getInitial('1')).isNull()
        }
    }

    @Test
    fun `중성 추출`() {
        assertSoftly { softly ->
            softly.assertThat(decomposer.getMedial('한')).isEqualTo('ㅏ')
            softly.assertThat(decomposer.getMedial('가')).isEqualTo('ㅏ')
            softly.assertThat(decomposer.getMedial('녕')).isEqualTo('ㅕ')
        }
    }

    @Test
    fun `중성 추출 - 한글이 아닌 경우`() {
        assertSoftly { softly ->
            softly.assertThat(decomposer.getMedial('A')).isNull()
            softly.assertThat(decomposer.getMedial('1')).isNull()
        }
    }

    @Test
    fun `종성 추출 - 받침 있는 경우`() {
        assertSoftly { softly ->
            softly.assertThat(decomposer.getFinal('한')).isEqualTo('ㄴ')
            softly.assertThat(decomposer.getFinal('녕')).isEqualTo('ㅇ')
            softly.assertThat(decomposer.getFinal('값')).isEqualTo('ㅄ')
        }
    }

    @Test
    fun `종성 추출 - 받침 없는 경우`() {
        assertSoftly { softly ->
            softly.assertThat(decomposer.getFinal('가')).isNull()
            softly.assertThat(decomposer.getFinal('나')).isNull()
        }
    }

    @Test
    fun `종성 추출 - 한글이 아닌 경우`() {
        assertSoftly { softly ->
            softly.assertThat(decomposer.getFinal('A')).isNull()
            softly.assertThat(decomposer.getFinal('1')).isNull()
        }
    }

    @Test
    fun `한글 여부 확인 - 한글인 경우`() {
        assertSoftly { softly ->
            softly.assertThat(decomposer.isHangul('가')).isTrue()
            softly.assertThat(decomposer.isHangul('한')).isTrue()
            softly.assertThat(decomposer.isHangul('힣')).isTrue()
            softly.assertThat(decomposer.isHangul('녕')).isTrue()
        }
    }

    @Test
    fun `한글 여부 확인 - 한글이 아닌 경우`() {
        assertSoftly { softly ->
            softly.assertThat(decomposer.isHangul('A')).isFalse()
            softly.assertThat(decomposer.isHangul('a')).isFalse()
            softly.assertThat(decomposer.isHangul('1')).isFalse()
            softly.assertThat(decomposer.isHangul('!')).isFalse()
            softly.assertThat(decomposer.isHangul(' ')).isFalse()
        }
    }

    @Test
    fun `DecomposedChar decomposed 프로퍼티 - 받침 있는 경우`() {
        val result = decomposer.decompose('한')

        assertEquals("ㅎㅏㄴ", result.decomposed)
    }

    @Test
    fun `DecomposedChar decomposed 프로퍼티 - 받침 없는 경우`() {
        val result = decomposer.decompose('가')

        assertEquals("ㄱㅏ", result.decomposed)
    }

    @Test
    fun `DecomposedChar decomposed 프로퍼티 - 한글이 아닌 경우`() {
        val result = decomposer.decompose('A')

        assertEquals("", result.decomposed)
    }

    @Test
    fun `다양한 초성 테스트`() {
        val testCases = mapOf(
            '가' to 'ㄱ',
            '까' to 'ㄲ',
            '나' to 'ㄴ',
            '다' to 'ㄷ',
            '따' to 'ㄸ',
            '라' to 'ㄹ',
            '마' to 'ㅁ',
            '바' to 'ㅂ',
            '빠' to 'ㅃ',
            '사' to 'ㅅ',
            '싸' to 'ㅆ',
            '아' to 'ㅇ',
            '자' to 'ㅈ',
            '짜' to 'ㅉ',
            '차' to 'ㅊ',
            '카' to 'ㅋ',
            '타' to 'ㅌ',
            '파' to 'ㅍ',
            '하' to 'ㅎ'
        )

        assertSoftly { softly ->
            testCases.forEach { (char, expectedChosung) ->
                softly.assertThat(decomposer.getInitial(char))
                    .`as`("$char 의 초성은 $expectedChosung 이어야 합니다")
                    .isEqualTo(expectedChosung)
            }
        }
    }

    @Test
    fun `다양한 중성 테스트`() {
        val testCases = mapOf(
            '가' to 'ㅏ',
            '개' to 'ㅐ',
            '갸' to 'ㅑ',
            '걔' to 'ㅒ',
            '거' to 'ㅓ',
            '게' to 'ㅔ',
            '겨' to 'ㅕ',
            '계' to 'ㅖ',
            '고' to 'ㅗ',
            '과' to 'ㅘ',
            '괘' to 'ㅙ',
            '괴' to 'ㅚ',
            '교' to 'ㅛ',
            '구' to 'ㅜ',
            '궈' to 'ㅝ',
            '궤' to 'ㅞ',
            '귀' to 'ㅟ',
            '규' to 'ㅠ',
            '그' to 'ㅡ',
            '긔' to 'ㅢ',
            '기' to 'ㅣ'
        )

        assertSoftly { softly ->
            testCases.forEach { (char, expectedJungsung) ->
                softly.assertThat(decomposer.getMedial(char))
                    .`as`("$char 의 중성은 $expectedJungsung 이어야 합니다")
                    .isEqualTo(expectedJungsung)
            }
        }
    }

    @Test
    fun `다양한 종성 테스트`() {
        val testCases = mapOf(
            '각' to 'ㄱ',
            '갂' to 'ㄲ',
            '갃' to 'ㄳ',
            '간' to 'ㄴ',
            '갅' to 'ㄵ',
            '갆' to 'ㄶ',
            '갇' to 'ㄷ',
            '갈' to 'ㄹ',
            '갉' to 'ㄺ',
            '갊' to 'ㄻ',
            '갋' to 'ㄼ',
            '갌' to 'ㄽ',
            '갍' to 'ㄾ',
            '갎' to 'ㄿ',
            '갏' to 'ㅀ',
            '감' to 'ㅁ',
            '갑' to 'ㅂ',
            '값' to 'ㅄ',
            '갓' to 'ㅅ',
            '갔' to 'ㅆ',
            '강' to 'ㅇ',
            '갖' to 'ㅈ',
            '갗' to 'ㅊ',
            '갘' to 'ㅋ',
            '같' to 'ㅌ',
            '갚' to 'ㅍ',
            '갛' to 'ㅎ'
        )

        assertSoftly { softly ->
            testCases.forEach { (char, expectedJongsung) ->
                softly.assertThat(decomposer.getFinal(char))
                    .`as`("$char 의 종성은 $expectedJongsung 이어야 합니다")
                    .isEqualTo(expectedJongsung)
            }
        }
    }

    @Test
    fun `빈 문자열 처리`() {
        val result = decomposer.decomposeAll("")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `빈 문자열 toString 변환`() {
        val result = decomposer.decomposeToString("")

        assertEquals("", result)
    }
}
