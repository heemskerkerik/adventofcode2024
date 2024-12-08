package org.nobody.day8

import org.nobody.utils.*

val testInput =
    """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent()

fun main() {
//    val input = testInput
    val input = downloadInput(8)

    val grid = parseCharGrid(input)
    val nodeTypes = grid.flatten().filter { it != '.' }.distinct().toList()

    fun withinBounds(point: Point): Boolean =
        point.x >= 0 && point.x < grid.size
          && point.y >= 0 && point.y < grid[0].size

    val antiNodes = nodeTypes.map {
        sequence {
            val allNodes = grid.findAll(it)

            for (node in allNodes) {
                for (otherNode in allNodes - node) {
                    yield(node - (otherNode - node))
                }
            }
        }.toList()
    }.flatten()

    val phase1 = antiNodes.toSet().filter(::withinBounds).size
    println(phase1)

    val resonantAntiNodes = nodeTypes.map {
        sequence {
            val allNodes = grid.findAll(it)

            yieldAll(allNodes)

            for (node in allNodes) {
                for (otherNode in allNodes - node) {
                    val vector = otherNode - node

                    var position = node - vector
                    while (withinBounds(position)) {
                        yield(position)

                        position -= vector
                    }
                }
            }
        }.toList()
    }.flatten()

    val phase2 = resonantAntiNodes.toSet().size
    println(phase2)
}
