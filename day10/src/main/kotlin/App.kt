package org.nobody.day10

import org.nobody.utils.*

val testInput =
    """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent()

fun main() {
//    val input = testInput
    val input = downloadInput(10).trim()

    val grid = parseCharGrid(input).toDigitGrid()

    val trailheads = grid.findAll(0)

    val phase1 = trailheads.sumOf { scoreTrail(grid, it) }
    println(phase1)

    val phase2 = trailheads.sumOf { scoreTrail(grid, it, mutableSetOf()) }
    println(phase2)
}

const val endOfTrail: Byte = 9

fun scoreTrail(grid: List<List<Byte>>, point: Point): Int {
    val trailEnds = mutableSetOf<Point>()
    scoreTrail(grid, point, trailEnds)
    return trailEnds.size
}

fun scoreTrail(grid: List<List<Byte>>, point: Point, trailEnds: MutableSet<Point>): Int {
    val current = grid.at(point)

    if (current == endOfTrail) {
        trailEnds.add(point)
        return 1
    }

    val possibilities = grid.aroundStraight(point).filter { grid.at(it) == (current + 1).toByte() }

    return possibilities.sumOf { scoreTrail(grid, it, trailEnds) }
}