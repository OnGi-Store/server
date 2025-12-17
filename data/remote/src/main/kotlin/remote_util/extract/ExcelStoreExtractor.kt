package remote_util.extract

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.FileMagic
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import remote_data.store.excel.OfficialStoreDTO
import java.io.ByteArrayInputStream
import java.io.IOException

internal class ExcelStoreExtractor {
    private val log = LoggerFactory.getLogger(ExcelStoreExtractor::class.java)

    fun parseExcel(excelData: ByteArray): List<OfficialStoreDTO> {
        val workbook = getWorkbook(excelData)
        return extractStores(workbook)
    }

    private fun getWorkbook(excelData: ByteArray): Workbook = runCatching {
        ByteArrayInputStream(excelData).use { inputStream: ByteArrayInputStream ->
            val fileType: FileMagic = FileMagic.valueOf(inputStream)
            log.info("다운로드된 엑셀 파일 타입: $fileType")

            inputStream.reset()
            when (fileType) {
                FileMagic.OLE2 -> HSSFWorkbook(inputStream)
                FileMagic.OOXML -> XSSFWorkbook(inputStream)
                else -> throw IOException("지원하지 않는 엑셀 파일 형식입니다. 타입: $fileType")
            }
        }
    }.getOrElse { e: Throwable ->
        log.error("POI parse error", e)
        throw RuntimeException("❌ 엑셀 파일을 읽는 도중 오류 발생", e)
    }

    private fun extractStores(workbook: Workbook?): List<OfficialStoreDTO> {
        if (workbook == null) return emptyList()

        val sheet = workbook.getSheetAt(0)
        val stores = mutableListOf<OfficialStoreDTO>()

        for (row in sheet) {
            val firstCell = row.getCell(0)
            if (firstCell == null || !isNumeric(cell = firstCell)) continue

            stores += convertToStore(row = row)
        }

        log.info("✅ 총 {} 개의 매장 정보 추출 완료", stores.size)
        return stores
    }

    private fun convertToStore(row: Row): OfficialStoreDTO = OfficialStoreDTO(
        name = getString(row = row, columnIndex = NAME) ?: throw IllegalStateException("매장명(NAME)이 비어 있습니다."),
        address = getString(row = row, columnIndex = ADDRESS) ?: throw IllegalStateException("주소(ADDRESS)가 비어 있습니다."),
        category = getString(row = row, columnIndex = CATEGORY),
        phone = getString(row = row, columnIndex = PHONE),
        menu = getString(row = row, columnIndex = MENU_NAME),
        menuPrice = getString(row = row, columnIndex = MENU_PRICE),
        hasParking = parseBoolean(row = row, columnIndex = HAS_PARKING),
        hasTakeout = parseBoolean(row = row, columnIndex = HAS_TAKEOUT),
        hasDelivery = parseBoolean(row = row, columnIndex = HAS_DELIVERY),
        hasReservation = parseBoolean(row = row, columnIndex = HAS_RESERVATION),
        hasDividedRestroom = parseBoolean(row = row, columnIndex = HAS_DIVIDED_RESTROOM),
        allowsGroup = parseBoolean(row = row, columnIndex = ALLOWS_GROUP),
        hasWifi = parseBoolean(row = row, columnIndex = HAS_WIFI),
        allowsPets = parseBoolean(row = row, columnIndex = ALLOWS_PETS),
        hasKidsFacility = parseBoolean(row = row, columnIndex = HAS_KIDS_FACILITY),
        imageUrl = parseUrl(row = row)
    )

    private fun parseBoolean(row: Row, columnIndex: Int): Boolean = runCatching {
        val cell = row.getCell(columnIndex)
        !cell.stringCellValue.isBlank()
    }.getOrDefault(defaultValue = false)

    private fun getString(row: Row, columnIndex: Int): String? {
        val cell = row.getCell(columnIndex) ?: return null

        return when (cell.cellType) {
            CellType.STRING -> cell.stringCellValue.takeIf { it.isNotBlank() && it != "-" }
            CellType.NUMERIC -> cell.numericCellValue.toInt().toString()
            else -> null
        }
    }

    private fun parseUrl(row: Row): String? {
        val cell = row.getCell(IMAGE_URL) ?: return null
        return cell.hyperlink?.address
    }

    private fun isNumeric(cell: Cell): Boolean = runCatching {
        cell.stringCellValue.toInt()
    }.isSuccess

    companion object {
        const val CATEGORY = 1
        const val NAME = 2
        const val MENU_NAME = 3
        const val MENU_PRICE = 4
        const val PHONE = 5
        const val ADDRESS = 6
        const val HAS_PARKING = 7
        const val HAS_TAKEOUT = 8
        const val HAS_DELIVERY = 9
        const val HAS_RESERVATION = 10
        const val HAS_DIVIDED_RESTROOM = 11
        const val ALLOWS_GROUP = 12
        const val HAS_WIFI = 13
        const val ALLOWS_PETS = 14
        const val HAS_KIDS_FACILITY = 15
        const val IMAGE_URL = 21
    }
}
