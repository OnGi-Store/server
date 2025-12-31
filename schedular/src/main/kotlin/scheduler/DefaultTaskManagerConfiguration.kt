package scheduler

import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.TaskExecutionToken
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.TaskManager
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.TaskManagerConfiguration.TaskManagerName.Companion.toTaskManagerName
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.lock.TaskLockManagerConfiguration
import io.ktor.server.application.*

internal class DefaultTaskManagerConfiguration : TaskLockManagerConfiguration() {
    override fun createTaskManager(application: Application): TaskManager<out TaskExecutionToken> = DefaultTaskManager(
        application = application,
        name = name.toTaskManagerName()
    )
}
