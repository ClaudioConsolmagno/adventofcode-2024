package dev.claudio.adventofcode2024.support

import org.apache.commons.lang3.mutable.MutableLong

/**
 * A terrible bag implementation, might clean this up later...
 */
open class HashBag<E>(override val size: Int) : Bag<E>() {
    constructor(coll: Map<E, Long>) : this(coll.size) {
        coll.forEach{
            this.add(it.key, it.value)
        }
    }
    constructor(coll: Collection<E>) : this(coll.size) {
        coll.forEach{
            this.add(it)
        }
    }

    @Transient
    private val map: MutableMap<E, MutableLong> = mutableMapOf()

    override fun contains(element: E): Boolean {
        return map.contains(element)
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        return elements.all { map.contains(it) }
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun iterator(): Iterator<E> {
        TODO("Not yet implemented")
    }

    fun getMap(): MutableMap<E, MutableLong> {
        return map
    }

    override fun getCount(element: E): Long {
        return map[element]?.toLong() ?: 0L
    }

    override fun add(element: E): Boolean {
        return add(element, 1)
    }

    override fun add(element: E, nCopies: Long): Boolean {
//        map[element]?.add(nCopies) ?: map[element]
        map.merge(element, MutableLong(nCopies)) { it, it2 -> it.add(it2); it }
        return true
    }

    override fun remove(element: Any?, nCopies: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun uniqueSet(): Set<E> {
        return map.keys
    }

    override fun sizeOfBag(): Long {
        TODO("Not yet implemented")
    }

    override fun addAll(currentPairs: Bag<E>) {
        currentPairs.uniqueSet().forEach {
            this.add(it, currentPairs.getCount(it))
        }
    }
}
