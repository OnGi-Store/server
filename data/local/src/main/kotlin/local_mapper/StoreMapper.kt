package local_mapper

import Menu
import Store
import StoreDetail
import local_dao.MenuEntity
import local_dao.StoreDetailEntity
import local_dao.StoreEntity

internal object StoreMapper {

    fun StoreEntity.toStore(distance: Double) = Store(
        id = id.value,
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
        distance = distance
    )

    fun StoreDetailEntity.toDetail() = StoreDetail(
        storeId = store.id.value,
        hasParking = hasParking,
        hasTakeout = hasTakeout,
        hasDelivery = hasDelivery,
        hasReservation = hasReservation,
        hasDividedRestroom = hasDividedRestroom,
        allowsPets = allowsPets,
        hasWifi = hasWifi,
        hasKidsFacility = hasKidsFacility,
        allowsGroup = allowsGroup,
    )

    fun MenuEntity.toMenu() = Menu(
        id = id.value,
        storeId = store.id.value,
        name = name,
        price = price,
    )

    fun Iterable<MenuEntity>.toMenu() = map { it.toMenu() }
}
