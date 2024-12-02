package dev.claudio.adventofcode2024.support

import java.awt.Point

class Support {
    companion object {
        fun readFileAsListInt(fileName: String): List<Int>? = readFileAsListString(fileName)?.map { Integer.valueOf(it) }

        fun readFileAsListString(fileName: String): List<String>? =
            object {}.javaClass.classLoader.getResourceAsStream(fileName)
                ?.bufferedReader()
                ?.readLines()

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
    }
}

open class PointValue<T>(x : Int, y: Int, var value: T) : Point(x,y)
