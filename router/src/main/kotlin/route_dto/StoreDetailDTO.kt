package route_dto

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
internal data class StoreDetailDTO(
    val storeId: Uuid,
    val hasParking: Boolean,
    val hasTakeout: Boolean,
    val hasDelivery: Boolean,
    val hasReservation: Boolean,
    val hasDividedRestroom: Boolean,
    val allowsPets: Boolean,
    val hasWifi: Boolean,
    val hasKidsFacility: Boolean,
    val allowsGroup: Boolean
)
