import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for the `ConcurrentQueue` class to verify the correctness of
 * basic queue operations such as enqueue, dequeue, peek, and size.
 *
 * These tests are designed to assess the functional integrity of the queue
 * under single-threaded conditions to ensure that all operations perform as
 * expected when executed sequentially.
 *
 * Note: These tests do not simulate concurrent scenarios. To verify the
 * correctness of the ConcurrentQueue class in a multi-threaded or
 * concurrent environment, please refer to the ConcurrentQueueTest class.
 */
class ConcurrentQueueUnitTests {
    private val queue = ConcurrentQueue<Int>()

    @Test
    fun testQueueAdd() {
        assertEquals(null, queue.peek())
        queue.enqueue(1)
        assertEquals(1, queue.peek())
        queue.enqueue(7)
    }

    @Test
    fun testQueueAddRemove() {
        queue.enqueue(3)
        queue.enqueue(2)
        assertEquals(3, queue.dequeue())
        assertEquals(2, queue.peek())
        assertEquals(2, queue.dequeue())
        assertEquals(null, queue.peek())
        assertEquals(true, queue.isEmpty())
    }
}