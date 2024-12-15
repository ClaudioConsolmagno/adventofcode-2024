package dev.claudio.adventofcode2024

import com.google.ortools.Loader
import com.google.ortools.modelbuilder.LinearExpr
import com.google.ortools.modelbuilder.LinearExprBuilder
import com.google.ortools.modelbuilder.ModelBuilder
import com.google.ortools.modelbuilder.ModelSolver
import com.google.ortools.modelbuilder.SolveStatus
import com.google.ortools.modelbuilder.Variable
import dev.claudio.adventofcode2024.support.Support
import java.awt.Point


fun main() {
    Loader.loadNativeLibraries()
    val day = "day13"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
    println(day13part1(sampleList!!))
    println(day13part2(inputList!!))
}

fun day13part1(inputList: List<String>): Any {
    return parseInput(inputList).sumOf {
        calculate(it)
    }
}

fun day13part2(inputList: List<String>): Any {
    return parseInput(inputList).sumOf {
        calculate(it, 10000000000000.0)
    }
}

fun calculate(it: Machine, part2ConversionError: Double = 0.0): Long {
    val model = ModelBuilder()
    val infinity = Double.POSITIVE_INFINITY
    val a: Variable = model.newIntVar(0.0, infinity, "a")
    val b: Variable = model.newIntVar(0.0, infinity, "b")

    val xEquation: LinearExprBuilder = LinearExpr.newBuilder()
    xEquation.addTerm(a, it.buttonA.x.toDouble())
    xEquation.addTerm(b, it.buttonB.x.toDouble())
    model.addEquality(xEquation, it.prize.x.toDouble() + part2ConversionError)

    val yEquation: LinearExprBuilder = LinearExpr.newBuilder()
    yEquation.addTerm(a, it.buttonA.y.toDouble())
    yEquation.addTerm(b, it.buttonB.y.toDouble())
    model.addEquality(yEquation, it.prize.y.toDouble() + part2ConversionError)

    val objectiveFunction: LinearExprBuilder = LinearExpr.newBuilder()
    objectiveFunction.addTerm(a, 1.0)
    objectiveFunction.addTerm(b, 1.0)
    model.minimize(objectiveFunction)

    val modelSolver = ModelSolver("SCIP")
    val solve: SolveStatus = modelSolver.solve(model)
    if (solve == SolveStatus.OPTIMAL) {
        val pressesA = modelSolver.getValue(a)
        val pressesB = modelSolver.getValue(b)
        return (3*pressesA + pressesB).toLong()
    } else {
        return 0L
    }
}

private fun parseInput(inputList: List<String>): List<Machine> {
    return inputList.chunked(4).map {
        Machine(
            getPointFromInput(it[0]),
            getPointFromInput(it[1]),
            getPointFromInput(it[2]),
        )
    }
}

fun getPointFromInput(s: String): Point {
    val xPosStart = s.indexOfFirst { it == '+' || it == '=' } + 1
    val yPosStart = s.indexOfLast { it == '+' || it == '=' } + 1
    val xPosEnd = s.indexOfFirst { it == ',' }
    val yPosEnd = s.length
    fun pointToInt(x: Int, y: Int) = s.substring(x, y).toInt()
    return Point(pointToInt(xPosStart, xPosEnd), pointToInt(yPosStart, yPosEnd))
}

data class Machine (val buttonA: Point, val buttonB: Point, val prize: Point)
