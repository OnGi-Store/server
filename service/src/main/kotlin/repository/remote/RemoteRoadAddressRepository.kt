package repository.remote

import kotlinx.coroutines.flow.Flow
import Juso

interface RemoteRoadAddressRepository {
    fun getRoadAddress(rawAddress: String): Flow<Juso?>
}
