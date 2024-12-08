package org.nobody.day6

import org.nobody.utils.*

val testInput =
    """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent()

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    fun rightTurn(): Direction = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }
}

sealed class TraverseOutput

data class Traversal(val locations: Set<Location>): TraverseOutput()
class Loop: TraverseOutput()

data class Location(val x: Int, val y: Int)

fun main() {
//    val input = testInput
    val input = downloadInput(6)

    val grid = parseCharGrid(input)
    val startingLocation = grid.findOne('^').let { Location(it.second, it.first) }

    val locations = (grid.traverse(startingLocation) as Traversal).locations
    val phase1 = locations.size
    println(phase1)

    val phase2 = locations.count { grid.traverse(startingLocation, collectLocations = false, obstacle = it) is Loop }
    println(phase2)
}

fun List<List<Char>>.traverse(start: Location, collectLocations: Boolean = true, obstacle: Location? = null): TraverseOutput {
    var location = start
    var direction = Direction.NORTH

    val turns = mutableSetOf<Pair<Location, Direction>>()
    val locations = mutableSetOf<Location>()

    if (collectLocations) {
        locations.add(start)
    }

    moves@ while (true) {
        retries@ while (true) {
            val newLocation = location.stepTo(direction)

            if (newLocation.isOutOfBounds(this)) break@moves

            if (this[newLocation.y][newLocation.x] == '#'
                || newLocation == obstacle) {
                direction = direction.rightTurn()

                var turn = Pair(location, direction)
                if (turn in turns) {
                    return Loop()
                }
                turns.add(turn)
                continue@retries
            }

            if (collectLocations) {
                locations.add(newLocation)
            }
            location = newLocation
        }
    }

    return Traversal(locations)
}

fun Location.stepTo(direction: Direction): Location {
    val newX = when (direction) {
        Direction.NORTH -> this.x - 1
        Direction.SOUTH -> this.x + 1
        else -> this.x
    }
    val newY = when (direction) {
        Direction.EAST -> this.y + 1
        Direction.WEST -> this.y - 1
        else -> this.y
    }

    return Location(newX, newY)
}

fun Location.isOutOfBounds(grid: List<List<*>>): Boolean =
    this.x < 0 || this.x >= grid.size
      || this.y < 0 || this.y >= grid[0].size