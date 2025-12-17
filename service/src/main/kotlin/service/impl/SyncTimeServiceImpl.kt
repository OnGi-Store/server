package service.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import repository.local.SyncTimeRepository
import service.SyncTimeService
import service.util.TransactionUtil
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class SyncTimeServiceImpl(private val syncTimeRepository: SyncTimeRepository) : SyncTimeService {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override suspend fun getSyncTime(): Instant {
        val time: Instant? = syncTimeRepository.getAll().firstOrNull()
        if (time == null) throw RuntimeException(ERROR)
        return time
    }

    override suspend fun setSyncTime() = TransactionUtil.suspendedTransaction {
        syncTimeRepository.deleteAll()

        val syncTime: Instant = Clock.System.now()
        syncTimeRepository.create(time = syncTime)
        log.info("⏰ 동기화 실행 시간: $syncTime")
    }

    companion object {
        private const val ERROR = "동기화 시간을 가져오지 못했습니다: 저장소가 비어 있습니다."
    }
}
