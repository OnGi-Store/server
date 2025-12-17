package remote_data.road

import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteJuso(
    val roadAddr: String,        // 전체 도로명주소
    val roadAddrPart1: String,   // 도로명주소(참고항목 제외)
    val roadAddrPart2: String?,  // 도로명주소 참고항목 (nullable)
    val jibunAddr: String,       // 지번주소
    val engAddr: String,         // 도로명주소(영문)
    val zipNo: String,           // 우편번호
    val admCd: String,           // 행정구역코드
    val rnMgtSn: String,         // 도로명코드
    val bdMgtSn: String,         // 건물관리번호
    val detBdNmList: String?,    // 상세건물명 (nullable)
    val bdNm: String?,           // 건물명 (nullable)
    val bdKdcd: String,          // 공동주택여부 (1: 공동주택, 0: 비공동주택)
    val siNm: String,            // 시도명
    val sggNm: String,           // 시군구명
    val emdNm: String,           // 읍면동명
    val liNm: String?,           // 법정리명 (nullable)
    val rn: String,              // 도로명
    val udrtYn: String,          // 지하여부 (0: 지상, 1: 지하)
    val buldMnnm: Int,           // 건물본번
    val buldSlno: Int,           // 건물부번
    val mtYn: String,            // 산여부 (0: 대지, 1: 산)
    val lnbrMnnm: Int,           // 지번본번
    val lnbrSlno: Int,           // 지번부번
    val emdNo: String,           // 읍면동일련번호
)
