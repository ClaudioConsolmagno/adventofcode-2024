package dev.claudio.adventofcode2024.support

import java.awt.Point
import kotlin.math.absoluteValue

class Support {
    companion object {
        fun readFileAsListInt(fileName: String): List<Int>? = readFileAsListString(fileName)?.map { Integer.valueOf(it) }

        fun readFileAsListString(fileName: String): List<String>? =
            object {}.javaClass.classLoader.getResourceAsStream(fileName)
                ?.bufferedReader()
                ?.readLines()

        fun readFileAsListListChar(fileName: String): MutableList<MutableList<Char>>? =
            object {}.javaClass.classLoader.getResourceAsStream(fileName)
                ?.bufferedReader()
                ?.readLines()
                ?.map { it.toCharArray().toMutableList() }
                ?.toMutableList()

        fun stringTranspose(list: List<String>): List<String> {
            val tranposedList = mutableListOf<CharArray>()
            (1..list[0].length).forEach {
                tranposedList.add(" ".repeat(list.size).toCharArray())
            }
            list.forEachIndexed { index1, originalEntry ->
                tranposedList.forEachIndexed { index, it ->
                    it[index1] = originalEntry[index]
                }
            }
            return tranposedList.map { String(it) }
        }

        fun stringDiagonals(list: List<String>): List<String> {
            val charsForPerfectSquare = list.size - list[0].length
            val squaredList = if (list.size > list[0].length) {
                list.map { it + ".".repeat(charsForPerfectSquare) }
            } else {
                list + (0 until charsForPerfectSquare.absoluteValue).map { ".".repeat(list[0].length) }
            }
            val n = squaredList.size
            val result = mutableListOf<String>()
            // Get diagonals starting from first row
            (0 until n).forEach { col ->
                val diagonal = mutableListOf<Char>()
                var i = 0
                var j = col
                while (i < n && j < n) {
                    diagonal.add(squaredList[i++][j++])
                }
                result.add(diagonal.joinToString("") { it.toString() })
            }

            // Get diagonals starting from first column (excluding first element as it's already covered)
            (1 until n).forEach { row ->
                val diagonal = mutableListOf<Char>()
                var i = row
                var j = 0
                while (i < n && j < n) {
                    diagonal.add(squaredList[i++][j++])
                }
                result.add(diagonal.joinToString("") { it.toString() })
            }

            return result
        }

        fun Collection<Point>.printGrid() {
            val xSize: Int = this.maxOf { it.x }
            val ySize: Int = this.maxOf { it.y }
            print("   ")
            fun printXHeader() {
                (0..xSize).forEach {
                    val blankSeparator: String = when {
                        xSize > 50 && it < 10 -> " "
                        xSize > 50 -> ""
                        it < 10 -> "  "
                        it < 100 -> " "
                        else -> ""
                    }
                    if (it < 100) print("$it$blankSeparator")
                }
            }
            printXHeader()
            println()
            val pointsSet = this.toSet()
            val separator = if (xSize > 50) { " " } else { "  " }
            (0..ySize).forEach { y ->
                if (y < 10) { print(" $y ") } else if (y < 100){ print("$y ") } else { print("   ") }
                (0..xSize).forEach { x ->
                    if (pointsSet.contains(Point(x, y))) {
                        print("#$separator")
                    } else {
                        print(".$separator")
                    }
                }
                println(y)
            }
            print("   ")
            printXHeader()
            println()
        }

        fun Collection<Point>.printGridSimple() {
            val minX = this.minOf { it.x }
            val minY = this.minOf { it.y }
            val maxX = this.maxOf { it.x }
            val maxY = this.maxOf { it.y }
            (minY..maxY).forEach { y ->
                (minX..maxX).forEach { x ->
                    if (this.contains(Point(x, y))) {
                        print("#")
                    } else {
                        print(".")
                    }
                }
                println()
            }
        }

        fun Point.surroundingPoints8(maxPoint: Point): Set<Point> {
            return (-1..1).flatMap { x ->
                    (-1..1).mapNotNull { y ->
                        if (!(x == 0 && y == 0)) {
                            Point(this.x + x, this.y + y)
                        } else {
                            null
                        }
                    }
                }
                .filter { it.y > -1 && it.x > -1 && it.x <= maxPoint.x && it.y <= maxPoint.y }
                .toSet()
        }

        fun Point.surroundingPoints4(maxPoint: Point): Set<Point> {
            return this.surroundingPoints8(maxPoint).filter { it.x == this.x || it.y == this.y }.toSet()
        }

        fun <T> Collection<PointValue<T>>.surroundingPoints8(target: Point): Set<PointValue<T>> {
            return setOfNotNull(
                this.get(target.x - 1, target.y - 1),
                this.get(target.x, target.y - 1),
                this.get(target.x + 1, target.y - 1),
                this.get(target.x - 1, target.y),
                this.get(target.x + 1, target.y),
                this.get(target.x - 1, target.y + 1),
                this.get(target.x, target.y + 1),
                this.get(target.x + 1, target.y + 1),
            )
        }

        fun <T> Collection<PointValue<T>>.addPadding(value: T): Set<PointValue<T>> {
            val minX = this.minOf { it.x } - 1
            val minY = this.minOf { it.y } - 1
            val maxX = this.maxOf { it.x } + 1
            val maxY = this.maxOf { it.y } + 1
            val result = this.toMutableSet()
            (minY..maxY).forEach { y ->
                result.add(PointValue(minX, y, value))
                result.add(PointValue(maxX, y, value))
            }
            (minX..maxX).forEach { x ->
                result.add(PointValue(x, minY, value))
                result.add(PointValue(x, maxY, value))
            }
            return result.onEach { it.translate(1, 1) }
        }

        fun <T> Collection<PointValue<T>>.removePadding(): Set<PointValue<T>> {
            val minX = this.minOf { it.x }
            val minY = this.minOf { it.y }
            val maxX = this.maxOf { it.x }
            val maxY = this.maxOf { it.y }
            return this.filterNot { it.x == minX || it.x == maxX || it.y == minY || it.y == maxY }
                .onEach { it.translate(-1, -1) }
                .toSet()
        }

        fun <T> Collection<PointValue<T>>.get(value: Point): PointValue<T>? {
            return this.get(value.x, value.y)
        }

        fun <T> Collection<PointValue<T>>.get(x: Int, y: Int): PointValue<T>? {
            return this.firstOrNull { it.x == x && it.y == y }
        }

        fun <T> List<T>.permutations(positions: Int): List<List<T>> {
            if (positions <= 0) return emptyList()

            var results = listOf(emptyList<T>())

            repeat(positions) {
                results = results.flatMap { current ->
                    this.map { op ->
                        current + op
                    }
                }
            }

            return results
        }

        inline fun <T> Collection<T>.toArrayDeque(): ArrayDeque<T> = ArrayDeque(this)
        inline fun <T> arrayDequeOf(vararg elements: T) = ArrayDeque(elements.toList())
        inline fun <T> ArrayDeque<T>.push(element: T) = addLast(element) // returns Unit
        inline fun <T> ArrayDeque<T>.pop() = removeLastOrNull()          // returns T?
    }
}

open class PointValue<T>(x : Int, y: Int, var value: T) : Point(x,y) {
    override fun toString(): String {
        return "[$x,$y,$value]"
    }
}
