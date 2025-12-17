package sync

data class StoreSyncResult(
    val createdCount: Long,
    val createFailCount: Long,
    val updatedCount: Long,
    val updateFailCount: Long
) {
    companion object {
        fun default() = StoreSyncResult(createdCount = 0, createFailCount = 0, updatedCount = 0, updateFailCount = 0)
    }
}
