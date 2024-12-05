package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support

fun main() {
    val day = "day3"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
    println(day3part2(sampleList!!))
    println(day3part2(inputList!!))
}

fun day3part1(inputList: List<String>): Any {
    return inputList
        .flatMap { findAllLegitMult(it) }
        .map { getMultPairs(it) }
        .sumOf { it.first * it.second }
}

fun getMultPairs(it: String): Pair<Long, Long> {
    val (left, right) = it.split(",")
    return Pair(
        "\\d\\d?\\d?".toRegex().find(left)!!.value.toLong(),
        "\\d\\d?\\d?".toRegex().find(right)!!.value.toLong(),
    )
}

fun findAllLegitMult(unparsedInputLine: String): List<String> {
    return "mul\\(\\d\\d?\\d?,\\d\\d?\\d?\\)".toRegex().findAll(unparsedInputLine).map { it.value }.toList()
}

fun day3part2(inputList: List<String>): Any {
    return findAllLegitMultToggled(inputList.joinToString { it })
        .map { getMultPairs(it) }
        .onEach { println(it) }
        .sumOf { it.first * it.second }
}

fun findAllLegitMultToggled(unparsedInputLine: String): List<String> {
    return "do\\(\\)".toRegex().split(unparsedInputLine)
        .map {
            "don't\\(\\)".toRegex().split(it)[0]
        }
        .flatMap {
            findAllLegitMult(it)
        }
}
