package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.PointValue
import dev.claudio.adventofcode2024.support.Support
import dev.claudio.adventofcode2024.support.Support.Companion.printGridSimple
import java.awt.Point

fun main() {
    val day = "day8"
    val sampleList = Support.readFileAsListListChar("2024/$day-sample.txt")
    val inputList = Support.readFileAsListListChar("2024/$day-input.txt")
    println(day8part2(sampleList!!))
    println(day8part2(inputList!!))
}

fun day8part1(inputList: List<List<Char>>): Any {
    return processWith(inputList) { a, b -> antinode(a, b, inputList.size) }
}

fun day8part2(inputList: List<List<Char>>): Any {
    return processWith(inputList) { a, b -> antinodeTFrequency(a, b, inputList.size) }
}

private fun processWith(inputList: List<List<Char>>, antinodeFunct: (PointValue<Char>, PointValue<Char>) -> List<Point>): Int {
    val gridSize = inputList.size
    val frequencies = inputList.mapIndexed { lineIndex, line ->
        line.mapIndexed { charIndex, char ->
            PointValue(charIndex, lineIndex, char)
        }.filterNot {
            it.value == '.'
        }
    }
    val freqOfSameType: Map<Char, List<PointValue<Char>>> = frequencies.flatten().groupBy { it.value }.filterValues { it.size != 1 }
    val antinodes = freqOfSameType.values.map { freqs ->
        val numFreqs = freqs.size
        freqs.flatMapIndexed() { index, pointValue ->
            (index..<(numFreqs - 1)).flatMap { freq ->
                antinodeFunct(pointValue, freqs[freq + 1])
                    .filter { it.x in 0..<gridSize && it.y in 0..<gridSize }
            }
        }
    }
    antinodes.flatten().printGridSimple()
    return antinodes.flatten().distinct().count()
}

fun antinode(a: Point, b: Point, gridSize: Int): List<Point> {
    val yDistance = (a.y - b.y)
    val xDistance = (a.x - b.x)
    return listOf(
        Point(a.x+xDistance, a.y+yDistance),
        Point(b.x-xDistance, b.y-yDistance)
    )
}

fun antinodeTFrequency(a: Point, b: Point, gridSize: Int): List<Point> {
    val xDistance = (a.x - b.x)
    val yDistance = (a.y - b.y)
    val left: List<Point> = generateSequence(a) { Point(it.x+xDistance, it.y+yDistance) }.takeWhile { it.x in 0..<gridSize && it.y in 0..<gridSize }.toList()
    val right: List<Point> = generateSequence(b) { Point(it.x-xDistance, it.y-yDistance) }.takeWhile { it.x in 0..<gridSize && it.y in 0..<gridSize }.toList()
    return listOf(a,b) + left + right
}
