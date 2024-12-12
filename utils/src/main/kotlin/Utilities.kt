package org.nobody.utils

import okhttp3.OkHttpClient
import okhttp3.Request

fun String.nonEmptyLines(): List<String> {
    return this.lines().filter { it.isNotBlank() }
}

fun parseTwoLists(input: String): Pair<List<String>, List<String>> {
    val lines = input.lines()
        .filter { it.isNotBlank() }
        .map { it.split(Regex("\\s+"), limit = 2) }

    val firstList = lines.map { it[0] }
    val secondList = lines.map { it[1] }

    return Pair(firstList, secondList)
}

fun parseSpacedGrid(input: String): List<List<String>> =
    input.lines()
        .filter { it.isNotBlank() }
        .map { it.split(Regex("\\s+")) }

fun parseCharGrid(input: String): List<List<Char>> =
    input.lines()
        .filter { it.isNotBlank() }
        .map { it.map { c -> c.toChar() } }

fun <T> List<List<T>>.findOne(needle: T): Point {
    for (row in 0..<this.size) {
        for (column in 0..<this[0].size) {
            if (this[row][column] == needle)
                return Point(column, row)
        }
    }

    throw Exception("Needle not found")
}

fun <T> List<List<T>>.findAll(needle: T): List<Point> {
    val result = mutableListOf<Point>()

    for (row in 0..<this.size) {
        for (column in 0..<this[0].size) {
            if (this[row][column] == needle)
                result.add(Point(column, row))
        }
    }

    return result
}

fun <T> List<List<T>>.at(point: Point): T {
    return this[point.y][point.x]
}

fun <T> List<List<T>>.aroundStraight(point: Point): List<Point> {
    val grid = this

    return sequence {
        if (point.y > 0) {
            yield(point.copy(y = point.y - 1))
        }
        if (point.x > 0) {
            yield(point.copy(x = point.x - 1))
        }
        if (point.x < grid[0].size - 1) {
            yield(point.copy(x = point.x + 1))
        }
        if (point.y < grid.size - 1) {
            yield(point.copy(y = point.y + 1))
        }
    }.toList()
}

fun List<List<Char>>.toDigitGrid(): List<List<Byte>> {
    return this.map {
        row -> row.map { c -> (c - '0').toByte() }
    }
}

fun downloadInput(day: Int): String {
    val client = OkHttpClient()
    val sessionToken = System.getenv("SESSION")

    val request = Request.Builder()
        .url("https://adventofcode.com/2024/day/$day/input")
        .get()
        .header("Cookie", "session=$sessionToken")
        .build()

    client.newCall(request).execute().use {
        require(it.isSuccessful)

        return it.body!!.string()
    }
}