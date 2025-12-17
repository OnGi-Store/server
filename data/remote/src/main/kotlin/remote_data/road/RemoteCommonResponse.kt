package remote_data.road

import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteCommonResponse(
    val totalCount: String,   // 총 검색 데이터 수
    val currentPage: Int,     // 페이지 번호
    val countPerPage: Int,    // 페이지당 결과 수
    val errorCode: String?,   // 에러 코드
    val errorMessage: String? // 에러 메시지
)
