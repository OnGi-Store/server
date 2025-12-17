package repository.remote

import RemoteStore

interface RemoteOfficialStoreRepository {
    suspend fun getStoreListFromOfficial(): List<RemoteStore>
}
