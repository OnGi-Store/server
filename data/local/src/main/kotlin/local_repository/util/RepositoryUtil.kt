package local_repository.util

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.math.*

internal object RepositoryUtil {
    private val dispatcher = Dispatchers.IO

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(context = dispatcher) {
        block()
    }

    // 하버사인 거리 구하기 공식
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}
