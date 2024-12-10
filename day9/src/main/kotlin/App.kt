package org.nobody.day9

import org.nobody.utils.*

val testInput =
    """2333133121414131402""".trimIndent()

sealed interface Node {
    var position: Int
    var size: Int
}

data class File(override var position: Int, override var size: Int, val id: Long) : Node
data class FreeSpace(override var position: Int, override var size: Int): Node

fun main() {
//    val input = testInput
    val input = downloadInput(9).trim()

    val nodes = sequence {
        var lastId = 0L
        var isFile = true
        var position = 0

        for (char in input.toCharArray()) {
            val size = char.digitToInt()
            if (isFile) {
                yield(File(position, size, lastId))
                lastId += 1
            } else {
                if (size > 0) {
                    yield(FreeSpace(position, size))
                }
            }

            isFile = !isFile
            position += size
        }
    }.toList()

    val files = nodes.filter { it is File }.map { it as File }.toList()
    val freeSpace = nodes.filter { it is FreeSpace }.map { it as FreeSpace }.toList()

    val phase1 = phase1(files, freeSpace)

    println(phase1)

    val phase2 = phase2(files, freeSpace)
    println(phase2)

//    println(checksum(phase2))
}

fun phase1(files: List<File>, freeSpaces: List<FreeSpace>): Long {
    val files = files.map { it.copy() }.toMutableList()
    val freeSpaces = freeSpaces.map { it.copy() }.toMutableList()

//    println(format(files, freeSpaces))

    while (freeSpaces.isNotEmpty()) {
        val file = files.maxBy { it.position }
        val freeSpace = freeSpaces.minBy { it.position }

        if (freeSpace.position > file.position) {
            break
        }

        if (file.size >= freeSpace.size) {
            val filePosition = file.position
            val remainingFile = file.copy(size = file.size - freeSpace.size, position = file.position + freeSpace.size)

            if (remainingFile.size > 0) {
                files.add(remainingFile)
            }
            file.position = freeSpace.position
            file.size = freeSpace.size

            freeSpace.position = filePosition
        } else {
            val freeSpacePosition = freeSpace.position
            freeSpace.position = freeSpace.position + file.size
            freeSpace.size = freeSpace.size - file.size
            file.position = freeSpacePosition
        }

//        println(format(files, freeSpaces))
    }

    return checksum(files)
}

fun phase2(files: List<File>, freeSpaces: List<FreeSpace>): Long {
    val files = files.map { it.copy() }.toMutableList()
    val freeSpaces = freeSpaces.map { it.copy() }.toMutableList()

//    println(format(files, freeSpaces))

    for (file in files.reversed()) {
        val freeSpace = freeSpaces.filter { it.position < file.position && it.size >= file.size }.minByOrNull { it.position }

        if (freeSpace == null) {
            //println("No free space for file $file")
            continue
        }

        val freeSpacePosition = freeSpace.position

        if (freeSpace.size > file.size) {
            freeSpace.size = freeSpace.size - file.size
            freeSpace.position = freeSpace.position + file.size
        } else {
            freeSpaces.remove(freeSpace)
        }

        file.position = freeSpacePosition

//        println(format(files, freeSpaces))
    }

    return checksum(files)
}

fun checksum(nodes: List<File>): Long {
    return nodes.fold(0L) { previous: Long, node: Node ->
        val finalPosition = (node.position + (node as File).size - 1).toInt()
        val nodeChecksum = (node.size.toLong() * (finalPosition + node.position) / 2) * node.id
        previous + nodeChecksum
    }
}

fun format(nodes: List<Node>): String {
    return nodes.joinToString("") {
        if (it is File) it.id.toString().repeat(it.size.toInt()) else ".".repeat((it as FreeSpace).size)
    }
}

fun format(files: List<File>, freeSpaces: List<FreeSpace>): String {
    val nodes = files.plus(freeSpaces).sortedBy { it.position  }
    return nodes.joinToString("") {
        if (it is File) it.id.toString().repeat(it.size.toInt()) else ".".repeat((it as FreeSpace).size)
    }
}
