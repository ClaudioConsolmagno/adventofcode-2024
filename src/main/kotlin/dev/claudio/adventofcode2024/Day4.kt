package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support

val xmasRegex = "XMAS".toRegex()
val masRegex = "MAS".toRegex()

fun main() {
    val day = "day4"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
    println(day4part2(sampleList!!))
    println(day4part2(inputList!!))
}

fun day4part1(inputList: List<String>): Any {
    val verticalList = Support.stringTranspose(inputList)
    val diagonalList = Support.stringDiagonals(inputList)
    val otherDiagonal = Support.stringDiagonals(verticalList.map { it.reversed() })
    return listOf(inputList, diagonalList, verticalList, otherDiagonal).sumOf { target ->
        target.sumOf { xmasRegex.findAll(it).count() } +
        target.sumOf { xmasRegex.findAll(it.reversed()).count() }
    }
}

fun day4part2(inputList: List<String>): Any {
    return (0..inputList.size-3).sumOf { idx ->
        val line1Window: List<String> = inputList[idx].windowed(3)
        val line2Window = inputList[idx+1].windowed(3)
        val line3Window = inputList[idx+2].windowed(3)
        line1Window.indices
            .map { listOf(line1Window[it], line2Window[it], line3Window[it]) }
            .count { isXmasMatch(it) }
    }
}

fun isXmasMatch(window2d: List<String>): Boolean {
    val verticalList = Support.stringTranspose(window2d)
    val diagonalList = Support.stringDiagonals(window2d)
    val otherDiagonal = Support.stringDiagonals(verticalList.map { it.reversed() })
    return listOf(diagonalList, otherDiagonal).sumOf { target ->
        target.sumOf { masRegex.findAll(it).count() } +
        target.sumOf { masRegex.findAll(it.reversed()).count() }
    } == 2
}
