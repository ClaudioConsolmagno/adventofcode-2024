package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.HashBag
import dev.claudio.adventofcode2024.support.Support
import kotlin.math.absoluteValue

fun main() {
    val day = "day1"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
    println(day1part2(sampleList!!))
    println(day1part2(inputList!!))
}

fun day1part1(inputList: List<String>): Any {
    val unzipped = inputList
        .map { it.split("   ") }
        .map { it[0].toInt() to it[1].toInt() }
        .unzip()
    val left = unzipped.first.sorted()
    val right = unzipped.second.sorted()
    return left.mapIndexed { index, value ->
        (value - right[index]).absoluteValue
    }.sum()
}

fun day1part2(inputList: List<String>): Any {
    val unzipped = inputList
        .map { it.split("   ") }
        .map { it[0].toInt() to it[1].toInt() }
        .unzip()
//    val bagLeft = HashBag(unzipped.first)
    val bagRight = HashBag(unzipped.second)
    return unzipped.first.mapIndexed { index, value ->
        value * bagRight.getCount(value)
    }
        .sum()
}
