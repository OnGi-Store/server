package remote_data.store.api

import kotlinx.serialization.Serializable

@Serializable
data class CloudPageDTO(
    val page: Long,
    val perPage: Long,
    val totalCount: Int,
    val currentCount: Long,
    val matchCount: Long,
    val data: List<CloudStoreDTO>
)

