package dev.claudio.adventofcode2024.support

abstract class Bag<E> : Collection<E> {
    abstract fun getCount(element: E): Long
    abstract fun add(element: E): Boolean
    abstract fun add(element: E, nCopies: Long): Boolean
    abstract fun remove(element: Any?, nCopies: Long): Boolean
    abstract fun uniqueSet(): Set<E>
    abstract fun sizeOfBag(): Long
    abstract fun addAll(currentPairs: Bag<E>)
}
