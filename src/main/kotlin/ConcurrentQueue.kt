import kotlinx.atomicfu.*

/**
 * Concurrent, lock-free queue implemented with Michael-Scott algorithm
 */
class ConcurrentQueue<T> {
    /**
     * Represents a node in the queue.
     * @param T the type of the item stored in the node
     * @property item the item stored in this node, or null if it is a dummy node
     */
    class Node<T>(val item: T?) {
        /**
         * The next node in the queue, initialized as null and updated atomically
         */
        val next = atomic<Node<T>?>(null)
    }

    /**
     * Dummy node used to initialize the head and tail pointers
     */
    private val dummy = Node<T>(null)

    /**
     * Initially points to the dummy node and is updated atomically
     */
    private val head = atomic(dummy)

    /**
     * Initially points to the dummy node and is updated atomically
     */
    private val tail = atomic(dummy)

    /**
     * Atomically adds the value to the end of the queue.
     *
     * 1. Appends the value wrapped in a new node to the `tail` if another coroutine has not already modified the `tail`
     * 2. Tries to adjust the `tail` to point to the last node if the `tail` has `next` value
     *
     * @param value the value to be added to the queue
     */
    fun enqueue(value: T) {
        val newNode = Node(value)
        while (true) {
            val currentTail = tail.value
            val tailNext = currentTail.next.value
            if (tail.value == currentTail) {
                if (tailNext == null) {
                    // Tries to link the new node to tail
                    if (currentTail.next.compareAndSet(null, newNode)) {
                        // Attempts to set the tail to the new node.
                        // If it fails, it means another node coming after this one became a tail already.
                        tail.compareAndSet(currentTail, newNode)
                        return
                    }
                } else {
                    // Tries to move the tail pointer forward if another coroutine has already added a next node
                    tail.compareAndSet(currentTail, tailNext)
                }
            }
        }
    }

    /**
     * Atomically removes and returns the value at the head.
     *
     * 1. Retrieves the value by the head and moves the head pointer to the next node
     * 2. Adjusts the head pointer if it's
     * @return the value at the front of the queue, or null if the queue is empty.
     */
    fun dequeue(): T? {
        while (true) {
            val currentHead = head.value
            val currentTail = tail.value
            val headNext = currentHead.next.value
            if (currentHead == head.value) {
                if (currentHead == currentTail) {
                    if (headNext == null) {
                        // The queue is empty
                        return null
                    }
                    // If the queue is not empty, move the tail forward to correct its position
                    tail.compareAndSet(currentTail, headNext)
                } else {
                    val value = headNext?.item
                    // Attempts to move the head pointer forward
                    if (head.compareAndSet(currentHead, headNext!!)) {
                        return value
                    }
                }
            }
        }
    }

    /**
     * Returns the value at the head of the queue without removing it.
     * @return the value at the front of the queue, or null if the queue is empty.
     */
    fun peek(): T? = head.value.next.value?.item

    /**
     * Checks if the queue is empty.
     * @return true if the queue is empty, false otherwise.
     */
    fun isEmpty(): Boolean = head.value == tail.value && head.value.next.value == null
}
