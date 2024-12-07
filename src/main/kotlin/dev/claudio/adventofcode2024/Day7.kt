package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support
import dev.claudio.adventofcode2024.support.Support.Companion.permutations
import kotlin.math.pow

fun main() {
    val day = "day7"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
    println(day7part2(sampleList!!))
    println(day7part2(inputList!!))
}

fun day7part1(inputList: List<String>): Any {
    val day7s: List<Day7> = day7s(inputList)
    return day7s.map { day7 ->
        val positions = (day7.equation.size - 1)
        val possibilities: List<List<Operator>> = (Operator.entries - Operator.CONCAT).permutations(positions)
        eval(day7, possibilities)
    }.sumOf { it }
}

fun day7part2(inputList: List<String>): Any {
    val day7s: List<Day7> = day7s(inputList)
    return day7s.map { day7 ->
        val positions = (day7.equation.size - 1)
        val possibilities: List<List<Operator>> = Operator.entries.permutations(positions)
        eval(day7, possibilities)
    }.sumOf { it } // 348360680516005
}

fun eval(day7: Day7, possibilities: List<List<Operator>>,): Long {
    val isMatch = possibilities.any { operands ->
        val testResult = day7.equation.reduceIndexed { index, acc, l ->
            when (operands[index-1]) {
                Operator.ADD -> acc + l
                Operator.MULT -> acc * l
                Operator.CONCAT -> acc * (10.0.pow(l.toString().length).toLong()) + l
            }
        }
        testResult == day7.result
    }
    return if (isMatch) {
        day7.result
    } else {
        0L
    }
}

private fun day7s(inputList: List<String>) = inputList.map {
    Day7(
        it.split(": ")[0].toLong(),
        it.split(": ")[1].split(" ").map { it.toLong() },
    )
}

data class Day7(val result: Long, val equation: List<Long>)

enum class Operator {
    ADD,
    MULT,
    CONCAT
}
