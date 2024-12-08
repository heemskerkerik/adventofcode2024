package org.nobody.day1

import org.nobody.utils.*

import kotlin.math.abs

val testInput =
    """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent()

fun main() {
//    val lists = parseTwoLists(testInput)
    val lists = parseTwoLists(downloadInput(1))

    val left = lists.component1().map { it.toInt() }.sorted()
    val right = lists.component2().map { it.toInt() }.sorted()

    val sum = left.zip(right) { a, b -> abs(a - b) }.sum()

    println(sum)

    val similarity = left.sumOf { it * right.count { r -> r == it } }

    println(similarity)
}
