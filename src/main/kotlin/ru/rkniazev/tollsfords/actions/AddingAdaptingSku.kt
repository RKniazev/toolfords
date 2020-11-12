import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import ru.rkniazev.tollsfords.controllers.MainController
import ru.rkniazev.tollsfords.models.AdaptingSku
import ru.rkniazev.tollsfords.models.AdaptingSkuRepository
import ru.rkniazev.tollsfords.models.SKU
import ru.rkniazev.tollsfords.models.SkuRepository
import java.io.File
import java.io.FileInputStream


class AddingAdaptingSku(path: String,
                        val repoSku: SkuRepository,
                        val addingAdaptingSku: AdaptingSkuRepository) {


    private val LOGGER = LoggerFactory.getLogger(MainController::class.java)

    private val excelFile = FileInputStream(File(path))
    private val workbook = XSSFWorkbook(excelFile)

    fun start(){
        val sheets = workbook.sheetIterator()
        while (sheets.hasNext()){
            val currentSheet = sheets.next()
            val rows = currentSheet.iterator()
            while (rows.hasNext()){
                analyseRow(rows.next())
            }
        }
    }

    private fun analyseRow(row: Row){
        try {
            if (row.count()!=0){
                var nameClient = ""
                var sku:SKU? = null
                for (cell in row.cellIterator()){
                    val any = when (cell.columnIndex) {
                        0 -> nameClient = cell.stringCellValue
                        1 -> sku = repoSku.findByName(cell.stringCellValue)?.first()
                        else -> {}
                    }
                }
                sku?.let { addingAdaptingSku.saveAndFlush(AdaptingSku(nameClient,sku)) }
            }
        } catch (E:Exception){
            println("Ошибка")
            for (cell in row.cellIterator()){
                println("${cell.columnIndex} - ${cell.stringCellValue}")
            }
            println()
        }

    }

}