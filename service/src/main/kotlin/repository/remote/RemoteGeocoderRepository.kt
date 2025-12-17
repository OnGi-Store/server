package repository.remote

import Point

interface RemoteGeocoderRepository {
    suspend fun getStoreLocation(address: String): Point
}
