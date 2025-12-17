package local_repository.impl

import StoreDetail
import local_dao.StoreDetailEntity
import local_dao.StoreEntity
import local_mapper.StoreMapper.toDetail
import repository.local.StoreDetailRepository
import local_repository.util.RepositoryUtil.dbQuery
import local_table.StoreDetailTable
import local_table.StoreTable
import java.util.*

internal class StoreDetailRepositoryImpl : StoreDetailRepository {
    override suspend fun findByStoreId(storeId: UUID): StoreDetail? = dbQuery {
        StoreDetailEntity
            .find { StoreDetailTable.id eq storeId }
            .singleOrNull()
            ?.toDetail()
    }

    override suspend fun create(storeDetail: StoreDetail): StoreDetail = dbQuery {
        val storeEntity = StoreEntity.find { StoreTable.id eq storeDetail.storeId }
            .singleOrNull()
            ?: throw NoSuchElementException("ID ${storeDetail.storeId}를 가진 StoreDetail을 찾을 수 없습니다.")

        StoreDetailEntity.new {
            store = storeEntity
            hasParking = storeDetail.hasParking
            hasTakeout = storeDetail.hasTakeout
            hasDelivery = storeDetail.hasDelivery
            hasReservation = storeDetail.hasReservation
            hasDividedRestroom = storeDetail.hasDividedRestroom
            allowsPets = storeDetail.allowsGroup
            hasWifi = storeDetail.hasWifi
            hasKidsFacility = storeDetail.hasKidsFacility
            allowsGroup = storeDetail.allowsGroup
        }.toDetail()
    }

    override suspend fun update(storeDetail: StoreDetail): StoreDetail = dbQuery {
        val entity = StoreDetailEntity
            .find { StoreDetailTable.id eq storeDetail.storeId }
            .singleOrNull()
            ?: throw NoSuchElementException("ID ${storeDetail.storeId}를 가진 StoreDetail을 찾을 수 없습니다.")

        entity.apply {
            hasParking = storeDetail.hasParking
            hasTakeout = storeDetail.hasTakeout
            hasDelivery = storeDetail.hasDelivery
            hasReservation = storeDetail.hasReservation
            hasDividedRestroom = storeDetail.hasDividedRestroom
            allowsPets = storeDetail.allowsPets
            hasWifi = storeDetail.hasWifi
            hasKidsFacility = storeDetail.hasKidsFacility
            allowsGroup = storeDetail.allowsGroup
        }

        entity.toDetail()
    }
}
