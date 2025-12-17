package remote_mapper

import Juso
import remote_data.road.RemoteJuso

internal object JusoMapper {
    fun RemoteJuso.toJuso() = Juso(
        roadAddress = roadAddr,
        sigunguName = sggNm,
        eubMyeonDongName = emdNm
    )
}
