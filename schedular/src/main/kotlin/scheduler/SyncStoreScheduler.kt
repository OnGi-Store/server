package scheduler

import io.github.flaxoos.ktor.server.plugins.taskscheduling.TaskScheduling
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import service.SyncStoreService
import service.SyncTimeService
import sync.StoreSyncResult
import kotlin.time.Duration.Companion.minutes

private const val STORE_SCHEDULAR = "STORE_SCHEDULAR"

fun Application.configureTaskScheduling() {
    val syncTimeService: SyncTimeService by dependencies
    val syncStoreService: SyncStoreService by dependencies
    val log: Logger = LoggerFactory.getLogger(javaClass)

    launch(context = Dispatchers.IO) {
        delay(duration = 1.minutes)
        log.info("🧪 테스트용 착한가게 동기화 시작...")
        executeSyncTask(syncTimeService, syncStoreService, log)
    }

    install(plugin = TaskScheduling) {
        addTaskManager(taskManagerConfiguration = DefaultTaskManagerConfiguration().apply { name = STORE_SCHEDULAR })
        task(taskManagerName = STORE_SCHEDULAR) {
            dispatcher = Dispatchers.IO
            concurrency = 1
            kronSchedule = {
                hours { at(value = 18) }
                minutes { at(value = 0) }
                seconds { at(value = 0) }
            }

            task = {
                log.info("📢 UTC 18:00 (KST 03:00) 데이터 동기화 시작")
                executeSyncTask(syncTimeService, syncStoreService, log)
            }
        }
    }
}

private suspend fun executeSyncTask(
    syncTimeService: SyncTimeService,
    syncStoreService: SyncStoreService,
    log: Logger
) = runCatching {
    coroutineScope {
        val syncBanner = async { syncStoreService.syncBanner() }
        val syncStore = async { syncStoreService.syncStoreData() }
        syncBanner.await() to syncStore.await()
    }
}.onSuccess { (bannerCount: Int, storeResult: StoreSyncResult) ->
    syncTimeService.setSyncTime()
    with(receiver = storeResult) {
        log.info("📦 착한가게 동기화 완료 - 🆕 신규 등록: ${createdCount}개, 🔄 업데이트: ${updatedCount}개")
        log.warn("⚠️ 착한가게 동기화 부분 실패 - ➕❌  신규 실패: ${createFailCount}개, ♻❌ 업데이트 실패: ${updateFailCount}개")
        log.info("🖼️ 배너 동기화 완료 - ✅ 추가된 배너: ${bannerCount}개")
    }
}.onFailure { error ->
    log.error("❌ 착한가게 동기화 중 에러 발생: ${error.message}", error)
}
