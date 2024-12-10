package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support
import dev.claudio.adventofcode2024.support.Support.Companion.surroundingPoints4
import java.awt.Point

fun main() {
    val day = "day10"
    val sampleList = Support.readFileAsListListChar("2024/$day-sample.txt")
    val inputList = Support.readFileAsListListChar("2024/$day-input.txt")
    println(day10part2(sampleList!!))
    println(day10part2(inputList!!))
}

fun day10part1(inputList: List<List<Char>>): Any {
    val inputListInt: List<List<Int>> = inputList.map { row -> row.map { it.digitToInt() } }
    return inputListInt.indices.flatMap { y ->
        inputListInt[y].indices.map { x ->
            if (inputListInt[y][x] == 0) {
                trailheads1(x, y, inputListInt, 0).count()
            }
            else 0
        }
    }.sum()
}

fun day10part2(inputList: List<List<Char>>): Any {
    val inputListInt: List<List<Int>> = inputList.map { row -> row.map { it.digitToInt() } }
    return inputListInt.indices.flatMap { y ->
        inputListInt[y].indices.map { x ->
            if (inputListInt[y][x] == 0) {
                trailheads2(x, y, inputListInt, 0)
            }
            else 0
        }
    }.sum()
}

fun trailheads2(x: Int, y: Int, inputList: List<List<Int>>, value: Int): Int {
    if (value == 9) return 1
    return Point(x, y).surroundingPoints4(Point(inputList.size - 1, inputList.size - 1))
        .filter { inputList[it.y][it.x] == value + 1 }
        .sumOf { trailheads2(it.x, it.y, inputList, value + 1) }
}

fun trailheads1(x: Int, y: Int, inputList: List<List<Int>>, value: Int): Set<Point> {
    if (value == 9) return setOf(Point(x, y))
    return Point(x, y).surroundingPoints4(Point(inputList.size - 1, inputList.size - 1))
        .filter { inputList[it.y][it.x] == value + 1 }
        .flatMap { trailheads1(it.x, it.y, inputList, value + 1) }
        .toSet()
}
