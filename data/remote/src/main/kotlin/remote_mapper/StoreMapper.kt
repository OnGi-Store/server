package remote_mapper

import Point
import RemoteMenu
import RemoteStore
import org.slf4j.LoggerFactory
import remote_data.geocode.response.RemotePoint
import remote_data.store.api.CloudStoreDTO
import remote_data.store.excel.OfficialStoreDTO

internal object StoreMapper {
    private val log = LoggerFactory.getLogger(javaClass)

    private fun CloudStoreDTO.extractMenu(): Set<RemoteMenu> = buildSet {
        if (!menu1.isNullOrBlank() && !price1.isNullOrBlank()) add(RemoteMenu(name = menu1, price = price1))
        if (!menu2.isNullOrBlank() && !price2.isNullOrBlank()) add(RemoteMenu(name = menu2, price = price2))
        if (!menu3.isNullOrBlank() && !price3.isNullOrBlank()) add(RemoteMenu(name = menu3, price = price3))
        if (!menu4.isNullOrBlank() && !price4.isNullOrBlank()) add(RemoteMenu(name = menu4, price = price4))
    }

    fun CloudStoreDTO.toRemoteStoreOrNull(): RemoteStore? {
        if (address.isNullOrBlank()) {
            log.warn("⚠️ 주소 누락으로 매장 스킵됨: name={}, city={}, district={}", name, city, district)
            return null
        }
        
        if(category.isNullOrBlank()) {
            log.warn("⚠️ 카테고리 누락으로 매장 스킵됨: name={}, city={}, district={}", name, city, district)
            return null
        }

        return RemoteStore(
            name = name,
            address = address,
            category = category,
            phone = phone,
            city = city,
            district = district,
            menus = extractMenu(),
            imageUrl = null
        )
    }

    fun List<CloudStoreDTO>.toRemoteStoreFromCloud(): List<RemoteStore> = mapNotNull { it.toRemoteStoreOrNull() }

    private fun OfficialStoreDTO.extractMenu(): Set<RemoteMenu> = buildSet {
        if (!menu.isNullOrBlank() && !menuPrice.isNullOrBlank()) add(RemoteMenu(name = menu, price = menuPrice))
    }

    fun OfficialStoreDTO.toRemoteStore() = RemoteStore(
        name = name,
        address = address,
        imageUrl = imageUrl,
        category = category,
        phone = phone,
        city = "",
        district = "",
        menus = extractMenu(),
        hasParking = hasParking,
        hasTakeout = hasTakeout,
        hasDelivery = hasDelivery,
        hasReservation = hasReservation,
        hasDividedRestroom = hasDividedRestroom,
        allowsGroup = allowsGroup,
        hasWifi = hasWifi,
        allowsPets = allowsPets,
        hasKidsFacility = hasKidsFacility,
    )

    fun List<OfficialStoreDTO>.toRemoteStoreFromOfficial(): List<RemoteStore> = map { it.toRemoteStore() }

    fun RemotePoint.toPoint() = Point(
        longitude = x,
        latitude = y,
    )
}