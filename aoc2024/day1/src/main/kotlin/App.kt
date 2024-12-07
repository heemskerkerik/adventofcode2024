package org.nobody.day1

import org.nobody.utils.Util

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
    val lists = Util.parseTwoLists(Util.downloadInput(1))
//    val lists = Util.parseTwoLists(testInput)

    val left = lists.component1().map { it.toInt() }.sorted()
    val right = lists.component2().map { it.toInt() }.sorted()

    val sum = left.zip(right) { a, b -> abs(a - b) }.sum()

    println(sum)

    val similarity = left.sumOf { it * right.count { r -> r == it } }

    println(similarity)
}
