package service

import sync.StoreSyncResult

interface SyncStoreService {
    suspend fun syncBanner(): Int
    suspend fun syncStoreData(): StoreSyncResult
}
