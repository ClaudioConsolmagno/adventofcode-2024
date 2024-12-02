package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support
import kotlin.math.absoluteValue

fun main() {
    val day = "day2"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
    println(day2part2(sampleList!!))
    println(day2part2(inputList!!))
}

fun day2part1(inputList: List<String>): Any {
    val levelsList = inputList
        .map { it.split(" ") }
        .map { it.map { level -> level.toInt() } }
    return levelsList
        .filter { levels ->
            day2IsSafe(levels)
        }.count()
}

private fun day2IsSafe(levels: List<Int>): Boolean {
    val windowed = levels.windowed(2, 1)
    return windowed.all { window ->
        val absoluteValue = (window[0] - window[1]).absoluteValue
        (absoluteValue == 1 || absoluteValue == 2 || absoluteValue == 3)
    }
            && (windowed.all { it[0] < it[1] } || windowed.all { it[0] > it[1] })
}

fun day2part2(inputList: List<String>): Any {
    val levelsList = inputList
        .map { it.split(" ") }
        .map { it.map { level -> level.toInt() } }
    return levelsList
        .filter { levels ->
            val allVariations = levels.indices.map { l1 -> levels.filterIndexed { l2, i -> l1 != l2 } }
            day2IsSafe(levels) || allVariations.count { day2IsSafe(it) } > 0
        }.count()
}
