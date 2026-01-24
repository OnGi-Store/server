package local_table

import local_util.DatabaseUtil.timestamp
import local_util.DatabaseUtil.uuidPrimaryKey
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private const val STORE_TABLE = "store"
private const val NAME = "name"
private const val ADDRESS = "address"
private const val FAVORITE_COUNT = "favoriteCount"
private const val LATITUDE = "latitude"
private const val LONGITUDE = "longitude"
private const val UPDATED_AT = "updatedAt"
private const val CATEGORY = "category"
private const val PHONE = "phone"
private const val CITY = "city"
private const val DISTRICT = "district"
private const val IMAGE_URL = "imageUrl"
private const val GEOHASH = "geohash"
private const val UNIQUE_NAME = "idx_$GEOHASH"

@OptIn(ExperimentalTime::class)
internal object StoreTable : IdTable<UUID>(name = STORE_TABLE) {
    override val id: Column<EntityID<UUID>> = uuidPrimaryKey()
    override val primaryKey = PrimaryKey(columns = arrayOf(id))
    val name: Column<String> = varchar(name = NAME, length = 255)
    val address: Column<String> = varchar(name = ADDRESS, length = 255)
    val favoriteCount: Column<Int> = integer(name = FAVORITE_COUNT).default(defaultValue = 0)
    val latitude: Column<Double> = double(name = LATITUDE)
    val longitude: Column<Double> = double(name = LONGITUDE)
    val updatedAt: Column<Instant> = timestamp(name = UPDATED_AT).clientDefault { Clock.System.now() }
    val category: Column<String?> = varchar(name = CATEGORY, length = 255).nullable()
    val phone: Column<String?> = varchar(name = PHONE, length = 255).nullable()
    val city: Column<String> = varchar(name = CITY, length = 255)
    val district: Column<String?> = varchar(name = DISTRICT, length = 255).nullable()
    val imageUrl: Column<String?> = varchar(name = IMAGE_URL, length = 255).nullable()
    val geohash: Column<String> = varchar(name = GEOHASH, length = 20)

    init {
        index(
            customIndexName = UNIQUE_NAME,
            isUnique = false,
            columns = arrayOf(geohash),
            functions = null,
            filterCondition = null
        )
    }
}
