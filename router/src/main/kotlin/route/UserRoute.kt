package route

import User
import io.ktor.http.*
import io.ktor.server.plugins.di.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import route_dto.LikeInfoResponseDTO
import route_dto.UserRequestDTO
import route_mapper.UserMapper.toUserResponseDTO
import route_util.RoutingCallHelper.getId
import service.FavoriteService
import service.UserService
import java.util.*

/**
 * User API base path
 *
 * 사용자 관련 API의 최상위 경로
 */
private const val PATH = "/api/v1/users"

/**
 * Sub paths
 *
 * 사용자 즐겨찾기 관련 하위 경로
 */
private const val PATH_FAVORITES = "favorites"

/**
 * Path parameters
 *
 * URL 경로에서 사용되는 파라미터 이름
 */
private const val USER_ID = "userId"
private const val STORE_ID = "storeId"

/**
 * 사용자 관련 모든 라우트를 등록한다.
 */
internal fun Route.userRoute() {
    val userService: UserService by application.dependencies
    val favoriteService: FavoriteService by application.dependencies

    route(PATH) {
        registerFindOrCreateUser(userService = userService)
        registerDeleteUser(userService = userService)
        registerUserFavoriteRoutes(favoriteService = favoriteService)
    }
}

/**
 * POST /api/v1/users
 *
 * 주소를 기준으로 사용자를 조회하고, 없으면 새로 생성한다.
 */
private fun Route.registerFindOrCreateUser(userService: UserService) = post(path = "") {
    val userRequest: UserRequestDTO = call.receive()
    val user: User = userService.findOrCreateUserByAddress(address = userRequest.address)
    call.respond(status = HttpStatusCode.OK, message = user.toUserResponseDTO())
}

/**
 * DELETE /api/v1/users/{userId}
 *
 * 사용자 ID를 기준으로 사용자를 삭제한다.
 */
private fun Route.registerDeleteUser(userService: UserService) = delete(path = "{$USER_ID}") {
    val userId: UUID = call.getId(paramName = USER_ID)
    val result: Boolean = userService.deleteUser(userId)
    call.respond(status = HttpStatusCode.OK, message = result)
}

/**
 * Registers favorite-related routes under a specific user.
 *
 * 특정 사용자 하위의 즐겨찾기 관련 라우트를 등록한다.
 *
 * Base path:
 * /api/v1/users/{userId}/favorites
 */
private fun Route.registerUserFavoriteRoutes(favoriteService: FavoriteService) =
    route(path = "{$USER_ID}/$PATH_FAVORITES") {
        registerGetStoreLike(favoriteService = favoriteService)
        registerToggleStoreLike(favoriteService = favoriteService)
    }

/**
 * GET /api/v1/users/{userId}/favorites/{storeId}
 *
 * 특정 사용자가 해당 매장을 즐겨찾기 했는지 여부를 조회한다.
 */
private fun Route.registerGetStoreLike(favoriteService: FavoriteService) = get(path = "{$STORE_ID}") {
    val userId: UUID = call.getId(paramName = USER_ID)
    val storeId: UUID = call.getId(paramName = STORE_ID)
    val result: Boolean = favoriteService.getFavorite(userId = userId, storeId = storeId)
    call.respond(status = HttpStatusCode.OK, message = LikeInfoResponseDTO(like = result))
}

/**
 * POST /api/v1/users/{userId}/favorites/{storeId}
 *
 * 특정 매장에 대한 즐겨찾기 상태를 토글한다.
 */
private fun Route.registerToggleStoreLike(favoriteService: FavoriteService) = post(path = "{$STORE_ID}") {
    val userId: UUID = call.getId(paramName = USER_ID)
    val storeId: UUID = call.getId(paramName = STORE_ID)
    val result: Boolean = favoriteService.toggleFavorite(userId = userId, storeId = storeId)
    call.respond(status = HttpStatusCode.OK, message = LikeInfoResponseDTO(like = result))
}
