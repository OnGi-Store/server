package service.mapper

import Menu
import Point
import RemoteMenu
import RemoteStore
import Store
import StoreDetail
import java.util.*

internal object StoreMapper {
    fun RemoteStore.toStore(point: Point) = Store(
        id = UUID.randomUUID(),
        name = name,
        address = address,
        favoriteCount = 0,
        latitude = point.latitude,
        longitude = point.longitude,
        category = category,
        phone = phone,
        city = city,
        district = district,
        imageUrl = imageUrl,
        distance = Double.NaN
    )

    fun RemoteStore.toStoreDetail(storeId: UUID) = StoreDetail(
        storeId = storeId,
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

    fun RemoteStore.toStoreMenuList(storeId: UUID): List<Menu> = menus.map { menu: RemoteMenu ->
        Menu(
            id = UUID.randomUUID(),
            storeId = storeId,
            name = menu.name,
            price = menu.price
        )
    }.toList()
}
