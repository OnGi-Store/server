package local_repository.impl

import Banner
import local_dao.BannerEntity
import local_mapper.BannerMapper.toBanner
import org.jetbrains.exposed.sql.deleteAll
import repository.local.BannerRepository
import local_repository.util.RepositoryUtil.dbQuery
import local_table.BannerTable

internal class BannerRepositoryImpl : BannerRepository {
    override suspend fun findAll(): List<Banner> = dbQuery {
        BannerEntity.all().map { entity: BannerEntity -> entity.toBanner() }
    }

    override suspend fun createAll(bannerList: List<Banner>): List<Banner> = dbQuery {
        bannerList.map { banner: Banner ->
            BannerEntity.new {
                url = banner.url
                imageUrl = banner.imageUrl
            }
        }.map { entity: BannerEntity ->
            entity.toBanner()
        }
    }

    override suspend fun deleteAll(): Int = dbQuery {
        BannerTable.deleteAll()
    }
}
