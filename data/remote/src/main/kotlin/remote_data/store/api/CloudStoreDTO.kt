package remote_data.store.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CloudStoreDTO(

    @SerialName("시도")
    val city: String,

    @SerialName("시군")
    val district: String?,

    @SerialName("업종")
    val category: String?,

    @SerialName("업소명")
    val name: String,

    @SerialName("연락처")
    val phone: String?,

    @SerialName("주소")
    val address: String?,

    @SerialName("메뉴1")
    val menu1: String? = null,

    @SerialName("가격1")
    val price1: String? = null,

    @SerialName("메뉴2")
    val menu2: String? = null,

    @SerialName("가격2")
    val price2: String? = null,

    @SerialName("메뉴3")
    val menu3: String? = null,

    @SerialName("가격3")
    val price3: String? = null,

    @SerialName("메뉴4")
    val menu4: String? = null,

    @SerialName("가격4")
    val price4: String? = null
)
