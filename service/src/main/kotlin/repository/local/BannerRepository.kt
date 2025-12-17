package repository.local

import Banner

interface BannerRepository {
    suspend fun findAll(): List<Banner>
    suspend fun createAll(bannerList: List<Banner>): List<Banner>
    suspend fun deleteAll(): Int
}
