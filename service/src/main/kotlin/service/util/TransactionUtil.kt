package service.util

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

internal object TransactionUtil {
    private val dispatcher = Dispatchers.IO

    suspend fun <T> suspendedTransaction(block: suspend () -> T): T = newSuspendedTransaction(context = dispatcher) {
        block()
    }
}
