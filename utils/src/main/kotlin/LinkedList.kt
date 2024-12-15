package org.nobody.utils

class LinkedList<T>(): Iterable<LinkedList.Node<T>> {
    var head: NodeImpl<T>? = null
        private set
    var tail: NodeImpl<T>? = null
        private set

    constructor(elements: Iterable<T>): this() {
        for (element in elements) {
            add(element)
        }
    }

    fun add(element: T): Node<T> {
        val node = NodeImpl(element, previous = tail, next = null)

        if (head == null) {
            head = node
        }
        tail?.next = node

        tail = node
        return node
    }

    @Suppress("DuplicatedCode")
    fun insertAfter(node: Node<T>, element: T): Node<T> {
        require(node is NodeImpl)

        val after = node.next as NodeImpl?

        val newNode = NodeImpl(element, previous = node, next = after)

        if (after != null) {
            after.previous = newNode
        }
        node.next = newNode

        if (tail === node) {
            tail = newNode
        }

        return newNode
    }

    @Suppress("DuplicatedCode")
    fun insertBefore(node: Node<T>, element: T): Node<T> {
        require(node is NodeImpl)

        val before = node.previous as NodeImpl?

        val newNode = NodeImpl(element, previous = before, next = node)

        if (before != null) {
            before.next = newNode
        }
        node.previous = newNode

        if (head === node) {
            head = newNode
        }

        return newNode
    }

    override fun iterator(): Iterator<Node<T>> {
        return object : Iterator<Node<T>> {
            private var current: NodeImpl<T>? = null

            override fun hasNext(): Boolean {
                return (current == null && head != null) || current!!.next != null
            }

            override fun next(): Node<T> {
                if (current == null) {
                    current = head!!
                    return current!!
                } else {
                    current = current!!.next!! as NodeImpl<T>
                    return current!!
                }
            }
        }
    }

    interface Node<T> {
        var value: T
        val previous: Node<T>?
        val next: Node<T>?
    }

    data class NodeImpl<T>(override var value: T, override var previous: Node<T>?, override var next: Node<T>?) : Node<T>
}