package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support

fun main() {
    val day = "day6"
    val sampleList = Support.readFileAsListListChar("2024/$day-sample.txt")
    val inputList = Support.readFileAsListListChar("2024/$day-input.txt")
    println(day6part2(sampleList!!))
    println(day6part2(inputList!!))
}

val directions = listOf('^','>', 'v', '<')

enum class DIRECTION(val char: Char) {
    UP('^'),
    RIGHT('>'),
    DOWN('v'),
    LEFT('<'),
}

fun day6part1(inputList: List<List<Char>>): Any {
    val visitedPositions = findVisitedPositions(inputList)
    return visitedPositions.count()
}

fun day6part2(inputList: MutableList<MutableList<Char>>): Any {
    var currentPos: Triple<Int, Int, DIRECTION> = findCurrentPosition(inputList)
    val potentialObstructionPositions = findVisitedPositions(inputList) - (currentPos.first to currentPos.second)
    var loopCheckCounter = 0
    potentialObstructionPositions.forEach { obstructionPos ->
        inputList[obstructionPos.first][obstructionPos.second] = '#'
        if (loopCheck(currentPos, inputList)) loopCheckCounter++
        inputList[obstructionPos.first][obstructionPos.second] = '.'
    }

    return loopCheckCounter
}

private fun loopCheck(
    initialPos: Triple<Int, Int, DIRECTION>,
    inputList: List<List<Char>>,
): Boolean {
    var currentPos = initialPos
    val visitedPositions = mutableSetOf<Triple<Int, Int, DIRECTION>>()
    visitedPositions.add(currentPos)
    var exiting = false
    var stuckInLoop = false
    while (!exiting && !stuckInLoop) {
        while (!exiting && !stuckInLoop && canMoveForward(inputList, currentPos)) {
            currentPos = moveForward(inputList, currentPos)
            stuckInLoop = !visitedPositions.add(currentPos)
            exiting = exiting(inputList, currentPos)
        }
        currentPos = turn(currentPos)
    }
    return stuckInLoop
}

private fun findVisitedPositions(inputList: List<List<Char>>): Set<Pair<Int, Int>> {
    val visitedPositions = mutableSetOf<Triple<Int, Int, DIRECTION>>()
    var currentPos: Triple<Int, Int, DIRECTION> = findCurrentPosition(inputList)
    visitedPositions.add(currentPos)

    var exiting = false
    while (!exiting) {
        while (!exiting && canMoveForward(inputList, currentPos)) {
            currentPos = moveForward(inputList, currentPos)
            visitedPositions.add(currentPos)
            exiting = exiting(inputList, currentPos)
//            println(visitedPositions)
        }
        currentPos = turn(currentPos)
    }
    return visitedPositions.map { it.first to it.second }.toSet()
}

fun exiting(inputList: List<List<Char>>, currentPos: Triple<Int, Int, DIRECTION>): Boolean {
    return when (currentPos.third) {
        DIRECTION.UP -> currentPos.first-1 < 0
        DIRECTION.RIGHT -> currentPos.second+1 >= inputList[currentPos.first].size
        DIRECTION.DOWN -> currentPos.first+1 >= inputList.size
        DIRECTION.LEFT -> currentPos.second-1 < 0
    }
}

fun moveForward(inputList: List<List<Char>>, currentPos: Triple<Int, Int, DIRECTION>): Triple<Int, Int, DIRECTION> {
    return when (currentPos.third) {
        DIRECTION.UP -> currentPos.copy(first = currentPos.first-1)
        DIRECTION.RIGHT -> currentPos.copy(second = currentPos.second+1)
        DIRECTION.DOWN -> currentPos.copy(first = currentPos.first+1)
        DIRECTION.LEFT -> currentPos.copy(second = currentPos.second-1)
    }
}

fun canMoveForward(inputList: List<List<Char>>, currentPos: Triple<Int, Int, DIRECTION>): Boolean {
    return when (currentPos.third) {
        DIRECTION.UP -> inputList[currentPos.first-1][currentPos.second] != '#'
        DIRECTION.RIGHT -> inputList[currentPos.first][currentPos.second+1] != '#'
        DIRECTION.DOWN -> inputList[currentPos.first+1][currentPos.second] != '#'
        DIRECTION.LEFT -> inputList[currentPos.first][currentPos.second-1] != '#'
    }
}

fun turn(currentPos: Triple<Int, Int, DIRECTION>): Triple<Int, Int, DIRECTION> {
    val nextPositionOrdinal = (currentPos.third.ordinal + 1) % directions.count()
    return currentPos.copy(third = DIRECTION.entries[nextPositionOrdinal])
}

fun findCurrentPosition(inputList: List<List<Char>>): Triple<Int, Int, DIRECTION> {
    inputList.forEachIndexed { y, chars ->
        chars.forEachIndexed { x, c ->
            if (c in directions) return Triple(y, x, DIRECTION.entries.first { it.char == c })
        }
    }
    throw RuntimeException("Not found")
}
