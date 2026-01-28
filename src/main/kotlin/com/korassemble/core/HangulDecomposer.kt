package com.korassemble.core

import com.korassemble.core.HangulConstants.CHOSUNGS
import com.korassemble.core.HangulConstants.HANGUL_END
import com.korassemble.core.HangulConstants.HANGUL_START
import com.korassemble.core.HangulConstants.HANGUL_START_CODE
import com.korassemble.core.HangulConstants.JONGSUNGS
import com.korassemble.core.HangulConstants.JUNGSUNGS

class HangulDecomposer {

    fun decompose(char: Char): DecomposedChar {
        val code = char.code

        if (char !in HANGUL_START..HANGUL_END) {
            return DecomposedChar(char, null, null, null)
        }

        val relativeCode = code - HANGUL_START_CODE
        val initialIndex = relativeCode / (21 * 28)
        val medialIndex = (relativeCode % (21 * 28)) / 28
        val finalIndex = relativeCode % 28

        val initial = CHOSUNGS[initialIndex]
        val medial = JUNGSUNGS[medialIndex]
        val final = if (finalIndex == 0) null else JONGSUNGS[finalIndex]

        return DecomposedChar(char, initial, medial, final)
    }

    fun decomposeAll(text: String): List<DecomposedChar> {
        return text.map {
            decompose(it)
        }
    }

    fun decomposeToString(text: String): String {
        return text.map { char ->
            val decomposed = decompose(char)
            if (decomposed.chosung == null) {
                expandDoubleConsonant(decomposed.original)
            } else {
                decomposed.decomposed
            }
        }.joinToString("")
    }

    fun decomposeToInitials(text: String): String {
        return text.map { char ->
            val decomposed = decompose(char)
            decomposed.chosung ?: decomposed.original
        }.joinToString("")
    }

    fun getInitial(char: Char): Char? {
        return decompose(char).chosung
    }

    fun getMedial(char: Char): Char? {
        return decompose(char).jungsung
    }

    fun getFinal(char: Char): Char? {
        return decompose(char).jongsung
    }

    fun isHangul(char: Char): Boolean {
        return char in HANGUL_START..HANGUL_END
    }
}

data class DecomposedChar(
    val original: Char,
    val chosung: Char?,
    val jungsung: Char?,
    val jongsung: Char?
) {

    val decomposed: String
        get() = buildString {
            chosung?.let { append(expandDoubleConsonant(it)) }
            jungsung?.let { append(it) }
            jongsung?.let { append(expandDoubleConsonant(it)) }
        }
}

internal fun expandDoubleConsonant(char: Char): String = when (char) {
    'ㄲ' -> "ㄱㄱ"
    'ㄸ' -> "ㄷㄷ"
    'ㅃ' -> "ㅂㅂ"
    'ㅆ' -> "ㅅㅅ"
    'ㅉ' -> "ㅈㅈ"
    else -> char.toString()
}