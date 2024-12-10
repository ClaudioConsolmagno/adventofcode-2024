package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support

fun main() {
    val day = "day9"
    val sampleList = Support.readFileAsListListChar("2024/$day-sample.txt")
    val inputList = Support.readFileAsListListChar("2024/$day-input.txt")
    println(day9part2(sampleList!!))
    println(day9part2(inputList!!))
}

fun day9part1(inputList: List<List<Char>>): Any {
    val diskMap = inputList[0]
        .windowed(2, 2, true)
        .mapIndexed { index, it -> FileFreeSpace(index, it[0].digitToInt(), it.getOrElse(1, { '0' } ).digitToInt()) }
    val revDiskMap = diskMap.reversed()
    val results = mutableSetOf<Compressed>()
    var counter = 0
    var currEndFileIndex = 0
    var endFile = revDiskMap[currEndFileIndex]!!
    for ((index, file) in diskMap.withIndex()) {
        results += (0..<file.size).map {
            Compressed(file.fileId, counter++)
        }
        while (file.freeSpace > 0 && endFile.fileId - file.fileId > 0) {
            if (endFile.size > 0) {
                endFile.size--
                file.freeSpace--
                results += Compressed(endFile.fileId, counter++)
            } else {
                endFile = revDiskMap[++currEndFileIndex]!!
            }
        }
        if (endFile.fileId - file.fileId < 0) break
    }
    return results.fold(0L) { acc, compressed -> acc + (compressed.fileId * compressed.position) }
}

fun day9part2(inputList: List<List<Char>>): Long {
    val diskMap = inputList[0]
        .windowed(2, 2, true)
        .mapIndexed { index, it -> FileFreeSpace(index, it[0].digitToInt(), it.getOrElse(1, { '0' } ).digitToInt()) }
        .toMutableList()
    var targetIndex = diskMap.size -1
    while (targetIndex > 0) {
        if (!reverseAddFiles(diskMap, targetIndex)) {
            targetIndex--
        }
    }
    var position = 0
    return diskMap.foldIndexed(0L) {index, acc, fileFreeSpace ->
        val res: Long = (position..<(position+fileFreeSpace.size)).sumOf {
            it * (fileFreeSpace.fileId.toLong())
        }
        position += fileFreeSpace.size + fileFreeSpace.freeSpace
        acc + res
    }
}

fun reverseAddFiles(diskMap: MutableList<FileFreeSpace>, targetIndex: Int): Boolean {
    val target = diskMap[targetIndex]
    if (target.moved) return false
    val matchIndex = diskMap.take(targetIndex).indexOfFirst { it.freeSpace >= target.size }
    if (matchIndex >= 0) {
        val fileFreeSpace = diskMap[matchIndex]
        diskMap[targetIndex - 1].freeSpace += (target.freeSpace + target.size)
        target.freeSpace = fileFreeSpace.freeSpace - target.size
        fileFreeSpace.freeSpace = 0
        target.moved = true
        diskMap.removeAt(targetIndex)
        diskMap.add(matchIndex + 1, target)
        return true; // moved file
    }
    return false // not moved
}

data class FileFreeSpace(val fileId: Int, var size: Int, var freeSpace: Int, var moved: Boolean = false)
data class Compressed(val fileId: Int, val position: Int)
