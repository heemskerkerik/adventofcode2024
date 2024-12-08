package org.nobody.day5

import org.nobody.utils.*

val testInput =
    """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13
        
        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent()

fun main() {
//    val input = testInput
    val input = downloadInput(5)

    val parts = input.split(Regex("\\n\\n"), limit = 2)
    val constraints =
        parts[0].nonEmptyLines()
            .map { it.split("|", limit = 2).let {p -> p[0].toInt() to p[1].toInt() } }
            .groupBy({ it.first }, { it.second })

    val updates =
        parts[1].nonEmptyLines()
            .map { it.split(",").map(String::toInt) }

    val phase1 = updates.filter { isValid(it, constraints) }
        .sumOf { it[it.size / 2] }
    println(phase1)

    val phase2 = updates.filter { !isValid(it, constraints) }
        .sumOf { fix(it, constraints).let { u -> u[u.size / 2] } }
    println(phase2)
}

fun isValid(update: List<Int>, constraints: Map<Int, List<Int>>): Boolean {
    return update.withIndex().all { (index, value) -> isValid(update, constraints, index, value) }
}

fun isValid(update: List<Int>, constraints: Map<Int, List<Int>>, index: Int, value: Int): Boolean {
    return constraints[value]?.all { update.indexOf(it).let { ci -> ci == -1 || ci > index  } } != false
}

fun fix(update: List<Int>, constraints: Map<Int, List<Int>>): List<Int> {
    val result = update.toMutableList()

    retry@ while (true) {
        updates@ for ((index, value) in result.withIndex()) {
            if (isValid(result, constraints, index, value)) continue@updates

            val lowestIndexOfConstraints = constraints[value]!!.map { update.indexOf(it) }.filter { it != -1 }.min()
            result.removeAt(index)
            result.add(lowestIndexOfConstraints, value)

            continue@retry
        }

        break@retry
    }

    return result
}
