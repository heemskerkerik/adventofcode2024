package org.nobody.utils

data class Point(val x: Int, val y: Int) {
    operator fun minus(other: Point): Point = Point(x - other.x, y - other.y)
    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)
}