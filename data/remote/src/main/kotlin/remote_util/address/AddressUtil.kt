package remote_util.address

internal object AddressUtil {

    /**
     * 원본 주소 문자열(rawAddress)을 정제하여 불필요한 상세 정보(층, 호수 등)를 제거합니다.
     *
     * 이 함수는 다음 순서로 주소를 정규화합니다:
     * 1. 쉼표로 구분된 호수 정보 제거 (예: ", 101호")
     * 2. 쉼표(,)를 공백으로 치환
     * 3. 층 정보 제거 (예: "3층", "지하1층")
     * 4. 단독으로 표기된 호수 정보 제거 (예: " 202호")
     * 5. 연속된 공백을 단일 공백으로 축소하고 양쪽 공백 제거
     *
     * @return 상세 정보가 제거되고 표준화된 주소 문자열
     */
    fun String.cleanedAddress(): String = this
        .trim()
        .replace(regex = """\s+""".toRegex(), " ")
        .substringBefore(',')
        .substringBefore('(')
        .replace(regex = """\s+\d*[층호].*""".toRegex(), "")
        .replace(regex = """\s+[상하지B]\d*층.*""".toRegex(), "")
        .replace(regex = """\s+\d+동.*""".toRegex(), "")
        .replace(regex = """\s+[가-힣]+동\s+\d+.*""".toRegex(), "")
        .replace(regex = """\.\S*""".toRegex(), "")
        // **핵심: 도로명 뒤 공백 없이 붙은 숫자 분리**
        .replace(regex = """([로길])(\d)""".toRegex(), "$1 $2")
        .replace(regex = """\s{2,}""".toRegex(), " ")
        .trim()
}

