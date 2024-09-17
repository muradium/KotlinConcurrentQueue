import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import kotlin.test.Test

/**
 * Lincheck test that checks the correctness of different
 * operations of a `ConcurrentQueue` in concurrent scenarios
 */
class ConcurrentQueueTest {
    private val queue = ConcurrentQueue<Int>()

    @Operation
    fun enqueue(item: Int) = queue.enqueue(item)

    @Operation
    fun dequeue(): Int? = queue.dequeue()

    @Operation
    fun peek(): Int? = queue.peek()

    @Operation
    fun isEmpty(): Boolean = queue.isEmpty()

    @Test
    fun modelCheckingTest() = ModelCheckingOptions().check(this::class)
}
