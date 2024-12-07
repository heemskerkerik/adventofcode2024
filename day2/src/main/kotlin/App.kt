package org.nobody.day2

import org.nobody.utils.Util

import kotlin.math.abs

val testInput =
    """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent()

fun main() {
    val rawGrid = Util.parseSpacedGrid(Util.downloadInput(2))
//    val rawGrid = Util.parseGrid(testInput)

    val grid = rawGrid.map { row -> row.map { it.toInt() } }

    println(grid.count { isSafe(it) })

    println(grid.count { isSafeOrCanBeSafe(it) })
}

fun isSafe(row: List<Int>): Boolean =
    (isOnlyIncreasing(row) || isOnlyDecreasing(row))
      && isSafeDistances(row)

fun isOnlyDecreasing(row: List<Int>): Boolean =
    row.zip(row.drop(1)).all { (a, b) -> a > b }

fun isOnlyIncreasing(row: List<Int>): Boolean =
    row.zip(row.drop(1)).all { (a, b) -> a < b }

fun isSafeDistances(row: List<Int>): Boolean =
    row.zip(row.drop(1)).all { (a, b) -> abs(a - b) >= 1 && abs(a - b) <= 3 }

fun isSafeOrCanBeSafe(row: List<Int>): Boolean {
    if (isSafe(row)) return true

    return (0..<row.size).any { isSafe(getListExceptIndex(row, it)) }
}

fun getListExceptIndex(row: List<Int>, index: Int): List<Int> {
    val before = if (index > 0) row.subList(0, index) else emptyList()
    val after = if (index < row.size - 1) row.subList(index + 1, row.size) else emptyList()

    return before + after
}