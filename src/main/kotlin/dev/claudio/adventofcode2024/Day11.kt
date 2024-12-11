package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support

fun main() {
    val day = "day11"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
    val day11part1 = day11part1(inputList!!)
    if(day11part1 != 189167) throw Error("Incorrect Logic: $day11part1")
//    println(day11part2(sampleList!!))
//    println(day11part2(inputList!!))
}

fun day11part1(inputList: List<String>): Int {
    return blinkTime(inputList, 25)
}

fun day11part2(inputList: List<String>): Int {
    return blinkTime(inputList, 75)
}

private fun blinkTime(inputList: List<String>, blinkCount: Int): Int {
    var inputAsInt: List<Long> = inputList.flatMap { it.split(" ") }.map { it.toLong() }
    val memo = mutableMapOf(0L to listOf(1L))
    repeat(blinkCount) { iteration ->
        println("Starting blink $iteration with inputList size ${inputAsInt.size}")
        inputAsInt = inputAsInt.flatMap { runRules(it, memo) }
    }
    return inputAsInt.count()
}

fun runRules(stone: Long, memo: MutableMap<Long, List<Long>>): List<Long> {
    val memoResult = memo[stone]
    if (memoResult != null) {
        return memoResult
    }
    val stoneString = stone.toString()
    val result = if (stoneString.length % 2 == 0) {
        val (left, right) = stoneString.chunked(stoneString.length / 2)
        listOf(
            left.toLong(),
            right.toLong()
        )
    } else {
        listOf(stone * 2024L)
    }
    memo[stone] = result
    return result
}
