package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support
import dev.claudio.adventofcode2024.support.Support.Companion.printGrid
import dev.claudio.adventofcode2024.support.Support.Companion.toCountMap
import java.awt.Point

val DAY14_GRID_SIZE_X: Long = 101L
val DAY14_GRID_SIZE_Y: Long = 103L
//val DAY14_GRID_SIZE_X: Long = 11L // sample
//val DAY14_GRID_SIZE_Y: Long = 7L // sample
val DAY14_GRID_SIZE_X_MID: Long = DAY14_GRID_SIZE_X / 2
val DAY14_GRID_SIZE_Y_MID: Long = DAY14_GRID_SIZE_Y / 2

fun main() {
    val day = "day14"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
//    println(day14part1(sampleList!!))
    println(day14part1(inputList!!))
    println(day14part2(inputList!!))
}

fun day14part1(inputList: List<String>): Any {
    val robots: List<Robot> = processInput(inputList)
    val robotsAfterTick = robots.map { tick(it, 100) }
    val quadrantRobots: Map<RobotQuadrant, List<Robot>> = robotsAfterTick.groupBy { it.getQuadrant() }
    return quadrantRobots.filterKeys { it != RobotQuadrant.OUTSIDE }
        .mapValues { (_,v) -> v.size }
        .values
        .reduce { acc, i -> acc * i }
}

fun day14part2(inputList: List<String>): Any {
    val robots: List<Robot> = processInput(inputList)
    var iterations = 0
    val xMasTree = generateSequence(robots) { input ->
            ++iterations
            input.map { tick(it, 1) }
        }
        .first { hasChristmasTree(it.map { it.position }) }
    xMasTree.map { it.position }.printGrid()
    return iterations
}

fun hasChristmasTree(robs: List<Point>): Boolean {
    val grid: Map<Int, Map<Int, Int>> = robs.toCountMap()
    return robs.any { treeMatch3Levels(it.x, it.y, grid) }
}

fun treeMatch3Levels(x: Int, y: Int, grid: Map<Int, Map<Int, Any>>): Boolean {
    return grid[y+1]?.get(x-1) != null &&
            grid[y+1]?.get(x+1) != null &&
            grid[y+2]?.get(x-2) != null &&
            grid[y+2]?.get(x+2) != null &&
            grid[y+3]?.get(x-3) != null &&
            grid[y+3]?.get(x+3) != null
}

fun tick(rob: Robot, iterations: Int): Robot {
    val vx = rob.velocity.x.toLong() * iterations
    val vy = rob.velocity.y.toLong() * iterations
    val vx2 = newPos(rob.position.x, vx, DAY14_GRID_SIZE_X)
    val vy2 = newPos(rob.position.y, vy, DAY14_GRID_SIZE_Y)
    val newPos = Point(vx2.toInt(), vy2.toInt())
    return rob.copy(position = newPos)
}

private fun newPos(pos: Int, vPos: Long, mod: Long): Long {
    val translated = (pos + vPos) % mod
    return if (translated >= 0) {
        translated
    } else {
        mod + translated
    }
}

fun processInput(inputList: List<String>): List<Robot> {
    return inputList.map {
        val equalsSplit = it.split("=")
        val pxy = equalsSplit[1].substringBefore(" ").split(",")
        val vxy = equalsSplit[2].split(",")
        Robot(Point(pxy[0].toInt(), pxy[1].toInt()), Point(vxy[0].toInt(), vxy[1].toInt()))
    }
}

data class Robot(val position: Point, val velocity: Point) {
    fun getQuadrant(): RobotQuadrant {
        if (position.x < DAY14_GRID_SIZE_X_MID && position.y < DAY14_GRID_SIZE_Y_MID) {
            return RobotQuadrant.TL
        } else if (position.x > DAY14_GRID_SIZE_X_MID && position.y < DAY14_GRID_SIZE_Y_MID) {
            return RobotQuadrant.TR
        } else if (position.x < DAY14_GRID_SIZE_X_MID && position.y > DAY14_GRID_SIZE_Y_MID) {
            return RobotQuadrant.BL
        } else if (position.x > DAY14_GRID_SIZE_X_MID && position.y > DAY14_GRID_SIZE_Y_MID) {
            return RobotQuadrant.BR
        } else {
            return RobotQuadrant.OUTSIDE
        }
    }
}

enum class RobotQuadrant {
    TL, TR, BL, BR, OUTSIDE
}
