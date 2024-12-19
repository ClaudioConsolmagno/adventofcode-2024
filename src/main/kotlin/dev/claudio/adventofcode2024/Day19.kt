package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support
import java.util.Collections

fun main() {
    val day = "day19"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
//    println(day19part2(sampleList!!))
    println(day19part2(inputList!!))
}

fun day19part1(inputList: List<String>): Any {
    val towelPatterns = inputList.takeWhile { it.isNotBlank() }.flatMap { it.split(", ").toList() }.toSet()
    val towelDesigns = inputList.takeLastWhile { it.isNotBlank() }
    return towelDesigns.count { designMatch(towelPatterns, it) }
}

fun designMatch(towelPatterns: Set<String>, towelDesign: String): Boolean {
    if (towelPatterns.contains(towelDesign) || towelDesign.isEmpty()) {
        return true
    }
    return towelPatterns
        .filter { towelDesign.startsWith(it) }
        .any { designMatch(towelPatterns, towelDesign.substring(it.length)) }
}

fun day19part2(inputList: List<String>): Any {
    val towelPatterns = inputList.takeWhile { it.isNotBlank() }.flatMap { it.split(", ").toList() }.toSet()
    val towelDesigns = inputList.takeLastWhile { it.isNotBlank() }
    var count = 1
    val cache = mutableMapOf<String, Long>()
    return towelDesigns.sumOf {
        println(count++)
        designMatchCount(towelPatterns, it, cache)
    }
}

fun designMatchCount(towelPatterns: Set<String>, towelDesign: String, cache: MutableMap<String, Long>): Long {
    cache[towelDesign]?.takeIf { return it }
    if (towelDesign.isEmpty()) return 0
    val match = if (towelPatterns.contains(towelDesign)) 1 else 0
    val res = match + towelPatterns
        .filter { towelDesign.startsWith(it) }
        .sumOf { designMatchCount(towelPatterns, towelDesign.substring(it.length), cache) }
    cache[towelDesign] = res
    return res
}
