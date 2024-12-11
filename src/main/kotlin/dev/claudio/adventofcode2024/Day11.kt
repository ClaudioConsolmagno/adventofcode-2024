package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support

fun main() {
    val day = "day11"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
    println(day11part1(sampleList!!))
    println(day11part1(inputList!!))
    println(day11part2(inputList!!))
}

fun day11part1(inputList: List<String>): Long {
    return blinkTime(inputList, 25)
}

fun day11part2(inputList: List<String>): Long {
    return blinkTime(inputList, 75)
}

private fun blinkTime(inputList: List<String>, blinkCount: Long): Long {
    val inputAsInt: List<Long> = inputList.flatMap { it.split(" ") }.map { it.toLong() }
    val memo = mutableMapOf<Pair<Long,Long>, Long>()
    var count = 1
    return inputAsInt.sumOf {
        println("Blinking ${count++}")
        runRules(it, memo, blinkCount-1)
    }
}

fun runRules(stone: Long, memo: MutableMap<Pair<Long,Long>, Long>, count: Long): Long {
    val memoResult = memo[stone to count]
    if (memoResult != null) {
        return memoResult
    }
    val stoneString = stone.toString()
    val result: List<Long> = if (stoneString.length % 2 == 0) {
        val (left, right) = stoneString.chunked(stoneString.length / 2)
        listOf(
            left.toLong(),
            right.toLong()
        )
    } else if (stone == 0L){
        listOf(1L)
    } else {
        listOf(stone * 2024L)
    }
    if (count == 0L) {
        memo[stone to count] = result.count().toLong()
    } else {
        memo[stone to count] = result.sumOf { runRules(it, memo, count-1) }
    }
    return memo[stone to count]!!
}
