package org.nobody.utils

import okhttp3.OkHttpClient
import okhttp3.Request

object Util {
    fun parseTwoLists(input: String): Pair<List<String>, List<String>> {
        val lines = input.lines()
            .filter { it.isNotBlank() }
            .map { it.split(Regex("\\s+"), limit = 2) }

        val firstList = lines.map { it[0] }
        val secondList = lines.map { it[1] }

        return Pair(firstList, secondList)
    }

    fun parseGrid(input: String): List<List<String>> =
        input.lines()
            .filter { it.isNotBlank() }
            .map { it.split(Regex("\\s+")) }

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
}
