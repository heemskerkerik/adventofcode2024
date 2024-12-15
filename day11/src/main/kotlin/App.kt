package org.nobody.day11

import org.nobody.utils.downloadInput
import kotlin.math.log10
import kotlin.math.pow

val testInput =
    """
        125 17
    """.trimIndent()

fun main() {
//    val input = testInput
    val input = downloadInput(11).trim()

    var gravestones = input.split(" ").map { it.toLong() }.groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    println(gravestones)

    repeat(25) {
        gravestones = blink(gravestones)
    }

    println(gravestones.values.sum())

    repeat(50) {
        gravestones = blink(gravestones)
    }

    println(gravestones.values.sum())
}

fun blink(gravestones: Map<Long, Long>): Map<Long, Long> {
    val result = mutableMapOf<Long, Long>().withDefault { 0 }

    fun add(key: Long, count: Long) {
        result[key] = result.getValue(key) + count
    }

    for (entry in gravestones.entries) {
        val count = entry.value
        if (entry.key == 0L) {
            add(1L, count)
        } else if (countDigits(entry.key) % 2 == 0) {
            val (left, right) = split(entry.key)

            add(left, count)
            add(right, count)
        } else {
            val newKey = entry.key * 2024L

            add(newKey, count)
        }
    }

    return result
}

fun countDigits(value: Long): Int {
    if (value == 0L) return 1
    return (log10(value.toDouble()) + 1).toInt()
}

fun split(value: Long): Pair<Long, Long> {
    val totalDigits = countDigits(value)
    val divisor = 10.0.pow(totalDigits / 2).toLong()
    return Pair(value / divisor.toLong(), value % divisor)
}