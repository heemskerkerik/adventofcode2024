package org.nobody.day3

import org.nobody.utils.*

val testInput =
    """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))""".trimIndent()

fun main() {
//    val input = testInput
    val input = downloadInput(3)

    val phase1 = Regex("mul\\((\\d+),(\\d+)\\)").findAll(input)
        .sumOf { it.groupValues[1].toInt() * it.groupValues[2].toInt() }

    println(phase1)

    val matches = Regex("mul\\((\\d+),(\\d+)\\)|do(?:n't)?\\(\\)?").findAll(input)

    val phase2 = sequence {
        var enabled = true

        for (match in matches) {
            when {
                match.value == "do()" -> enabled = true
                match.value == "don't()" -> enabled = false
                enabled -> yield(match.groupValues[1].toInt() * match.groupValues[2].toInt())
            }
        }
    }.sum()

    println(phase2)
}
