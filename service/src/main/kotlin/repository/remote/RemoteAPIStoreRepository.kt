package repository.remote

import RemoteStore

interface RemoteAPIStoreRepository {
    suspend fun getStoreListFromAPI(): List<RemoteStore>
}
