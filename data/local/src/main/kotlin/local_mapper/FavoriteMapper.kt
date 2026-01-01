package local_mapper

import Favorite
import local_dao.FavoriteEntity
import local_mapper.StoreMapper.toStore
import local_mapper.UserMapper.toUser

internal object FavoriteMapper {

    fun FavoriteEntity.toFavorite() = Favorite(
        id = id.value,
        user = user.toUser(),
        store = store.toStore()
    )

    fun List<FavoriteEntity>.toFavorite() = map { it.toFavorite() }
}
