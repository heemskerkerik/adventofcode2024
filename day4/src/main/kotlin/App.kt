package org.nobody.day4

import org.nobody.utils.*

val testInput =
    """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent()

fun main() {
//    val input = parseCharGrid(testInput)
    val input = parseCharGrid(downloadInput(4))

    val phase1 = sequence {
        for (row in 0..<input.size) {
            for (column in 0..<input[0].size) {
                if (matchesHorizontally(input, row, column))
                    yield(Triple(row, column, "H"))
                if (matchesVertically(input, row, column))
                    yield(Triple(row, column, "V"))
                if (matchesNorthEast(input, row, column, "XMAS"))
                    yield(Triple(row, column, "NE"))
                if (matchesSouthEast(input, row, column, "XMAS"))
                    yield(Triple(row, column, "SE"))
            }
        }
    }

    println(phase1.count())

    val phase2 = sequence {
        for (row in 0..<input.size) {
            for (column in 0..<input[0].size) {
                if (matchesSouthEast(input, row, column, "MAS") &&
                    matchesNorthEast(input, row + 2, column, "MAS"))
                    yield(Pair(row, column))
            }
        }
    }

    println(phase2.count())
}

fun matchesHorizontally(grid: List<List<Char>>, row: Int, column: Int): Boolean {
    if (column > grid[0].size - 4) return false

    return matches(
        "XMAS",
        grid[row][column],
        grid[row][column + 1],
        grid[row][column + 2],
        grid[row][column + 3]
    )
}

fun matches(needle: String, vararg chars: Char): Boolean {
    require(needle.length == chars.size)

    return (0..<chars.size).all { chars[it] == needle[it] }
      || (0..<chars.size).all { chars[it] == needle[needle.length - it - 1] }
}

fun matchesVertically(grid: List<List<Char>>, row: Int, column: Int): Boolean {
    if (row > grid.size - 4) return false

    return matches(
        "XMAS",
        grid[row][column],
        grid[row + 1][column],
        grid[row + 2][column],
        grid[row + 3][column]
    )
}

fun matchesNorthEast(grid: List<List<Char>>, row: Int, column: Int, needle: String): Boolean {
    if (row < needle.length - 1 || column > grid[0].size - needle.length) return false

    return matches(
        needle,
        *(0..<needle.length).map { grid[row - it][column + it] }.toCharArray()
    )
}

fun matchesSouthEast(grid: List<List<Char>>, row: Int, column: Int, needle: String): Boolean {
    if (row > grid.size - needle.length || column > grid[0].size - needle.length) return false

    return matches(
        needle,
        *(0..<needle.length).map { grid[row + it][column + it] }.toCharArray()
    )
}