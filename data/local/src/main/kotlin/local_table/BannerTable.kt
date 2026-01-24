package local_table

import local_util.DatabaseUtil.uuidPrimaryKey
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

private const val BANNER_TABLE = "banner"
private const val URL = "url"
private const val IMAGE_URL = "imageUrl"

internal object BannerTable : IdTable<UUID>(name = BANNER_TABLE) {
    override val id: Column<EntityID<UUID>> = uuidPrimaryKey()
    override val primaryKey = PrimaryKey(columns = arrayOf(id))
    val url: Column<String> = varchar(name = URL, length = 255)
    val imageUrl: Column<String> = varchar(name = IMAGE_URL, length = 255)
}
