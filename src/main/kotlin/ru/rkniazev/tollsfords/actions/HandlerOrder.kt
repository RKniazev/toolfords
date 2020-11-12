package ru.rkniazev.tollsfords.actions

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import ru.rkniazev.tollsfords.models.SKU
import ru.rkniazev.toolfords.Model.Order
import java.io.File
import java.io.FileInputStream
import java.lang.Exception

class HandlerOrder (val path:String) {
    private val excelFile = FileInputStream(File(path))
    private val workbook = XSSFWorkbook(excelFile)
    val order = Order()

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
        if (row.count()>=2){
            var name = ""
            var count = 0
            try {
                for (cell in row.cellIterator()){
                    when (cell.columnIndex){
                        1 -> name = cell.stringCellValue
                        2 -> count = cell.numericCellValue.toInt()
                        else -> Exception()
                    }
                }
                if (count>0){
                    order.add(SKU(name = name),count)
                }
            } catch (e:Exception){
            }
        }
    }

}