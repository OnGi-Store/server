package scheduler

import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.TaskManager
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.TaskManagerConfiguration
import io.github.flaxoos.ktor.server.plugins.taskscheduling.tasks.Task
import io.ktor.server.application.*
import korlibs.time.DateTime
import java.util.concurrent.ConcurrentHashMap

internal class DefaultTaskManager(
    override val application: Application,
    override val name: TaskManagerConfiguration.TaskManagerName
) : TaskManager<DefaultLock>() {
    private val lockStore = ConcurrentHashMap<String, DefaultLock>()

    override suspend fun attemptExecute(
        task: Task,
        executionTime: DateTime,
        concurrencyIndex: Int
    ): DefaultLock? {
        val key: String = lockKey(taskName = task.name, concurrencyIndex = concurrencyIndex)

        val lock: DefaultLock? = lockStore.compute(key) { _, existingLock: DefaultLock? ->
            return@compute if (existingLock == null || existingLock.lockedAt < executionTime) {
                DefaultLock(name = task.name, concurrencyIndex = concurrencyIndex, lockedAt = executionTime)
            } else {
                existingLock
            }
        }

        return if (lock?.lockedAt == executionTime) lock else null
    }

    override suspend fun init(tasks: List<Task>) = tasks.forEach { task: Task ->
        repeat(times = task.concurrency) { index: Int ->
            val key = lockKey(taskName = task.name, concurrencyIndex = index)
            val lockedAt = DateTime(unixMillis = 0.0)
            val defaultLock = DefaultLock(name = task.name, concurrencyIndex = index, lockedAt = lockedAt)
            lockStore.putIfAbsent(key, defaultLock)
        }
    }

    override fun close() = lockStore.clear()
    private fun lockKey(taskName: String, concurrencyIndex: Int): String = "$taskName:$concurrencyIndex"

}
