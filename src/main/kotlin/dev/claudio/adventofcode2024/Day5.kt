package dev.claudio.adventofcode2024

import dev.claudio.adventofcode2024.support.Support
import java.util.Collections

fun main() {
    val day = "day5"
    val sampleList: List<String>? = Support.readFileAsListString("2024/$day-sample.txt")
    val inputList: List<String>? = Support.readFileAsListString("2024/$day-input.txt")
//    println(day5part2(sampleList!!))
    println(day5part2(inputList!!))
}

fun day5part1(inputList: List<String>): Any {
    inputList.takeWhile { it.isNotEmpty() }
    val sectionSplitIndex = inputList.indexOfFirst { it.isEmpty() }
    val (rulesRaw, pageNumbersRaw) = inputList.subList(0, sectionSplitIndex - 1) to inputList.subList(sectionSplitIndex + 1, inputList.size)
    val rules: List<Pair<Int, Int>> = rulesRaw.map { it.split("|") }.map { Pair(it[0].toInt(), it[1].toInt()) }
    val pageNumbers: List<List<Int>> = pageNumbersRaw.map { it.split(",").map { it.toInt() } }
    val rulesIndex: Map<Int, Set<Int>> = createRulesIndex(rules)
    val correctUpdates : List<List<Int>> = findUpdatesInCorrectOrder(rules, rulesIndex, pageNumbers, true)
    return correctUpdates.sumOf { it[it.size / 2] }
}

fun findUpdatesInCorrectOrder(rules: List<Pair<Int, Int>>, rulesIndex: Map<Int, Set<Int>>, pageNumbers: List<List<Int>>, correctOrder: Boolean): List<List<Int>> {
    return pageNumbers.filter { updateSequence ->
        val inScopeRuleIndexes = updateSequence.mapNotNull { page -> rulesIndex[page] }.flatten().toSet()
        val rulesInScope: List<Pair<Int, Int>> = rules
            .filterIndexed { index, pair -> index in inScopeRuleIndexes }
            .filter { it.first in updateSequence && it.second in updateSequence }
        val b = validateUpdate(updateSequence, rulesInScope) == null
        b == correctOrder
    }
}

fun validateUpdate(updateSequence: List<Int>, rulesInScope: List<Pair<Int, Int>>): Pair<Int, Int>? {
    return rulesInScope.firstOrNull {
        val firstIndex = updateSequence.indexOf(it.first)
        firstIndex >= 0 && firstIndex > updateSequence.indexOf(it.second)
    }
}

fun createRulesIndex(rules: List<Pair<Int, Int>>): Map<Int, Set<Int>> {
    val rulesIndex: MutableMap<Int, MutableSet<Int>> = mutableMapOf()
    rules.forEachIndexed { index, pair ->
        rulesIndex.getOrPut(pair.first) { mutableSetOf() }.add(index)
        rulesIndex.getOrPut(pair.second) { mutableSetOf() }.add(index)
    }
    return rulesIndex
}

fun day5part2(inputList: List<String>): Any {
    inputList.takeWhile { it.isNotEmpty() }
    val sectionSplitIndex = inputList.indexOfFirst { it.isEmpty() }
    val (rulesRaw, pageNumbersRaw) = inputList.subList(0, sectionSplitIndex - 1) to inputList.subList(sectionSplitIndex + 1, inputList.size)
    val rules: List<Pair<Int, Int>> = rulesRaw.map { it.split("|") }.map { Pair(it[0].toInt(), it[1].toInt()) }
    val pageNumbers: List<List<Int>> = pageNumbersRaw.map { it.split(",").map { it.toInt() } }
    val rulesIndex: Map<Int, Set<Int>> = createRulesIndex(rules)
    val incorrectUpdates : List<List<Int>> = findUpdatesInCorrectOrder(rules, rulesIndex, pageNumbers, false)
    return incorrectUpdates
        .map { fixIncorrectUpdate(rules, rulesIndex, it) }
        .sumOf { it[it.size / 2] }
}

fun fixIncorrectUpdate(rules: List<Pair<Int, Int>>, rulesIndex: Map<Int, Set<Int>>, updateSequence: List<Int>): List<Int> {
    val inScopeRuleIndexes = updateSequence.mapNotNull { page -> rulesIndex[page] }.flatten().toSet()
    val rulesInScope: List<Pair<Int, Int>> = rules
        .filterIndexed { index, pair -> index in inScopeRuleIndexes }
        .filter { it.first in updateSequence && it.second in updateSequence }
    val failedRule = validateUpdate(updateSequence, rulesInScope)
    if (failedRule == null) {
        return updateSequence
    } else {
        val indexOf = updateSequence.indexOf(failedRule.first)
        val indexOf2 = updateSequence.indexOf(failedRule.second)
        val updateMutable = updateSequence.toMutableList()
        Collections.swap(updateMutable, indexOf, indexOf2)
        return fixIncorrectUpdate(rules, rulesIndex, updateMutable)
    }
}
