package scheduler

import io.github.flaxoos.ktor.server.plugins.taskscheduling.tasks.TaskLock
import korlibs.time.DateTime

internal data class DefaultLock(
    override val name: String,
    override val concurrencyIndex: Int,
    val lockedAt: DateTime
) : TaskLock


