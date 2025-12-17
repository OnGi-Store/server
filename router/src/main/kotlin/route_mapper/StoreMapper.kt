package route_mapper

import Menu
import Store
import StoreDetail
import StorePage
import route_dto.MenuDTO
import route_dto.StoreDTO
import route_dto.StoreDetailDTO
import route_dto.StorePageDTO
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

@OptIn(ExperimentalUuidApi::class)
internal object StoreMapper {
    fun Store.toStoreDTO() = StoreDTO(
        id = id.toKotlinUuid(),
        name = name,
        address = address,
        favoriteCount = favoriteCount,
        latitude = latitude,
        longitude = longitude,
        category = category,
        phone = phone,
        city = city,
        district = district,
        imageUrl = imageUrl,
        distance = distance,
    )

    fun StorePage.toStorePageDTO() = StorePageDTO(
        stores = stores.map { it.toStoreDTO() },
        hasNext = hasNext,
        hasPrev
    )

    fun StoreDetail.toStoreDetailDTO() = StoreDetailDTO(
        storeId = storeId.toKotlinUuid(),
        hasParking = hasParking,
        hasTakeout = hasTakeout,
        hasDelivery = hasDelivery,
        hasReservation = hasReservation,
        hasDividedRestroom = hasDividedRestroom,
        allowsPets = allowsPets,
        hasWifi = hasWifi,
        hasKidsFacility = hasKidsFacility,
        allowsGroup = allowsGroup
    )

    fun Menu.toMenuDTO() = MenuDTO(
        id = id.toKotlinUuid(),
        storeId = storeId.toKotlinUuid(),
        name = name,
        price = price,
    )

    fun List<Menu>.toMenuDTO() = map { it.toMenuDTO() }
}
