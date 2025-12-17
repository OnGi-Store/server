package remote_util.network

import kotlinx.coroutines.delay

object NetworkUtil {
    suspend fun <T> retryWithDelay(
        times: Int = 3,
        delayMillis: Long = 1_000,
        block: suspend () -> T
    ): T {
        repeat(times) { attempt ->
            runCatching {
                block()
            }.onSuccess {
                return it
            }.onFailure { error ->
                if (attempt == times - 1) throw error
                delay(timeMillis = delayMillis)
            }
        }
        throw IllegalStateException("예외 없이 재시도가 실패했습니다")
    }
}
