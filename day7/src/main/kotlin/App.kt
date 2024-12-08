package org.nobody.day7

import com.github.shiguruikai.combinatoricskt.Combinatorics
import org.nobody.utils.downloadInput
import org.nobody.utils.nonEmptyLines

val testInput =
    """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent()

fun main() {
//    val input = testInput
    val input = downloadInput(7)

    val sums = input.nonEmptyLines()
        .map { it.split(':', limit=2).let { it[0].toLong() to it[1].trim().split(' ').map { v -> v.toLong() } } }

    val phase1 = sums.filter { sum -> canSolve(sum).also { println("$sum: $it")} }.sumOf { it.first }
    println(phase1)

    val phase2 = sums.filter { sum -> canSolve(sum, includeConcat = true).also { println("$sum: $it")} }.sumOf { it.first }
    println(phase2)
}

fun canSolve(sum: Pair<Long, List<Long>>, includeConcat: Boolean = false): Boolean {
    val operations =
        if (!includeConcat)
            listOf<(Long, Long) -> Long>(Math::addExact, Math::multiplyExact)
        else
            listOf<(Long, Long) -> Long>(Math::addExact, Math::multiplyExact, ::concat)

    return Combinatorics.cartesianProduct(*List(sum.second.size - 1) { operations }.toTypedArray())
        .any { solve(sum, it) == sum.first }
}

fun solve(sum: Pair<Long, List<Long>>, operations: List<(Long, Long) -> Long>): Long =
    sum.second.drop(1).zip(operations).fold(sum.second.first()) { a, b -> b.second(a, b.first) }

fun concat(a: Long, b: Long): Long =
    (a.toString() + b.toString()).toLong()