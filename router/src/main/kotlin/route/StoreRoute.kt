package route

import Menu
import StoreDetail
import StorePage
import StoreWithDistance
import io.ktor.http.*
import io.ktor.server.plugins.di.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import query.StoreQueryCategory
import query.StoreQueryDistance
import query.StoreQuerySortType
import route_mapper.StoreMapper.toMenuDTO
import route_mapper.StoreMapper.toStoreDTO
import route_mapper.StoreMapper.toStoreDetailDTO
import route_mapper.StoreMapper.toStorePageDTO
import route_util.RoutingCallHelper.getDoubleParam
import route_util.RoutingCallHelper.getEnumParam
import route_util.RoutingCallHelper.getEnumParamOrNull
import route_util.RoutingCallHelper.getId
import route_util.RoutingCallHelper.getIntParam
import route_util.RoutingCallHelper.getParam
import route_util.RoutingCallHelper.getParamOrNull
import service.StoreService
import java.util.*

/**
 * Store API base path
 *
 * 가게 관련 API의 기본 경로
 */
private const val PATH = "/api/v1/stores"

/**
 * Sub paths
 *
 * 가게 관련 하위 경로
 */
private const val PATH_COUNT = "count"
private const val PATH_DETAIL = "detail"
private const val PATH_MENUS = "menus"

/**
 * Path parameters
 *
 * URL 경로에 사용되는 파라미터
 */
private const val STORE_ID = "storeId"
private const val USER_ID = "userId"

/**
 * Query parameters
 *
 * 조회 조건으로 사용되는 쿼리 파라미터
 */
private const val LATITUDE = "latitude"
private const val LONGITUDE = "longitude"
private const val PAGE = "page"
private const val SIZE = "size"
private const val SORT_TYPE = "sortType"
private const val CATEGORY = "category"
private const val DISTANCE = "distanceRange"
private const val KEYWORD = "keyword"
private const val ONLY_FAVORITES = "onlyFavorites"

/**
 * Default values
 *
 * 쿼리 파라미터 기본값
 */
private const val DEFAULT_LATITUDE = 37.5642135
private const val DEFAULT_LONGITUDE = 127.0016985
private const val DEFAULT_PAGE = 0
private const val DEFAULT_SIZE = 10
private val DEFAULT_SORT_TYPE = StoreQuerySortType.FAVORITE

/**
 * 가게 관련 모든 라우트를 등록한다.
 */
internal fun Route.storeRoute() {
    val storeService: StoreService by application.dependencies
    route(path = PATH) {
        registerGetStores(storeService = storeService)
        registerCountStores(storeService = storeService)
        registerStoreRoutes(storeService = storeService)
    }
}

/**
 * GET /api/v1/stores
 *
 * 조건에 맞는 가게 목록을 페이지 단위로 조회한다.
 */
private fun Route.registerGetStores(storeService: StoreService) = get(path = "") {
    val userId: UUID = call.getId(paramName = USER_ID)
    val lat: Double = call.getDoubleParam(name = LATITUDE, defaultValue = DEFAULT_LATITUDE)
    val lng: Double = call.getDoubleParam(name = LONGITUDE, defaultValue = DEFAULT_LONGITUDE)
    val page: Int = call.getIntParam(name = PAGE, defaultValue = DEFAULT_PAGE)
    val size: Int = call.getIntParam(name = SIZE, defaultValue = DEFAULT_SIZE)
    val sort: StoreQuerySortType = call.getEnumParam(name = SORT_TYPE, defaultValue = DEFAULT_SORT_TYPE)
    val category: StoreQueryCategory? = call.getEnumParamOrNull<StoreQueryCategory>(name = CATEGORY)
    val distance: StoreQueryDistance? = call.getEnumParamOrNull<StoreQueryDistance>(name = DISTANCE)
    val keyword: String? = call.getParamOrNull(name = KEYWORD, convert = { it })
    val onlyFavorite: Boolean = call.getParam(name = ONLY_FAVORITES, defaultValue = false, convert = { it.toBoolean() })
    val storePage: StorePage = storeService.getStores(
        userId = userId,
        latitude = lat,
        longitude = lng,
        page = page,
        size = size,
        sortType = sort,
        category = category,
        distance = distance,
        keyword = keyword,
        onlyFavorites = onlyFavorite
    )
    call.respond(status = HttpStatusCode.OK, message = storePage.toStorePageDTO())
}

/**
 * GET /api/v1/stores/count
 *
 * 가게 전체 개수를 조회한다.
 */
private fun Route.registerCountStores(storeService: StoreService) = get(path = PATH_COUNT) {
    val count: Long = storeService.contStores()
    call.respond(status = HttpStatusCode.OK, message = count)
}

/**
 * Registers store-specific routes.
 *
 * 특정 가게 ID 기준 하위 라우트를 등록한다.
 */
private fun Route.registerStoreRoutes(storeService: StoreService) = route(path = "{$STORE_ID}") {
    registerGetStore(storeService)
    registerGetStoreDetail(storeService)
    registerGetStoreMenus(storeService)
}

/**
 * GET /api/v1/stores/{storeId}
 *
 * 가게 기본 정보를 조회한다.
 */
private fun Route.registerGetStore(storeService: StoreService) = get(path = "") {
    val storeId: UUID = call.getId(paramName = STORE_ID)
    val lat: Double = call.getDoubleParam(name = LATITUDE, defaultValue = DEFAULT_LATITUDE)
    val lng: Double = call.getDoubleParam(name = LONGITUDE, defaultValue = DEFAULT_LONGITUDE)
    val store: StoreWithDistance = storeService.getStoreById(storeId = storeId, latitude = lat, longitude = lng)
    call.respond(status = HttpStatusCode.OK, message = store.toStoreDTO())
}

/**
 * GET /api/v1/stores/{storeId}/detail
 *
 * 가게의 상세 정보를 조회한다.
 */
private fun Route.registerGetStoreDetail(storeService: StoreService) = get(path = PATH_DETAIL) {
    val storeId: UUID = call.getId(paramName = STORE_ID)
    val storeDetail: StoreDetail = storeService.getStoreDetail(storeId = storeId)
    call.respond(status = HttpStatusCode.OK, message = storeDetail.toStoreDetailDTO())
}

/**
 * GET /api/v1/stores/{storeId}/menus
 *
 * 가게의 메뉴 목록을 조회한다.
 */
private fun Route.registerGetStoreMenus(storeService: StoreService) = get(path = PATH_MENUS) {
    val storeId: UUID = call.getId(paramName = STORE_ID)
    val menuList: List<Menu> = storeService.getStoreMenus(storeId = storeId)
    call.respond(status = HttpStatusCode.OK, message = menuList.toMenuDTO())
}
