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

fun List<List<Char>>.findOne(needle: Char): Pair<Int, Int> {
    for (row in 0..<this.size) {
        for (column in 0..<this[0].size) {
            if (this[row][column] == needle)
                return Pair(row, column)
        }
    }

    throw Exception("Needle not found")
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