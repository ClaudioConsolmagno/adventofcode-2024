package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support

fun main() {
    val day = "day1"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
    println(solution(sampleList!!))
}

fun solution(inputList: List<String>) {
    val summedUpCals: MutableList<Int> = mutableListOf(0)
    var counter = 0
    inputList.forEach {
        if (it.isBlank()) {
            counter++
            summedUpCals.add(0)
        } else {
            summedUpCals[counter] += Integer.valueOf(it)
        }
    }
    println(summedUpCals)
    println(summedUpCals.maxOrNull())
}
