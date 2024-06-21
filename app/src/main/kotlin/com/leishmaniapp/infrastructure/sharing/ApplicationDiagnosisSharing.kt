package com.leishmaniapp.infrastructure.sharing

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.Style
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.services.IDiagnosisSharing
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.toJavaLocalDateTime
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/**
 * Share a [Diagnosis] into PDF format
 */
class ApplicationDiagnosisSharing @Inject constructor(
    @ApplicationContext val context: Context,
) : IDiagnosisSharing {
    override suspend fun generateDiagnosisFile(diagnosis: Diagnosis): File {

        // Create the file
        val pdfFile = withContext(Dispatchers.IO) {
            File.createTempFile("diagnosis", ".pdf")
        }

        // Generate PDF metadata
        val pdfDoc = PdfDocument(PdfWriter(pdfFile))
        val document = Document(pdfDoc)

        val dateStr = DateTimeFormatter
            .ofPattern("dd/MM/yyyy - HH:mm", Locale.getDefault())
            .format(diagnosis.date.toJavaLocalDateTime())

        val titleStyle = Style()
            .setBackgroundColor(DeviceRgb(99, 34, 105))
            .setTextAlignment(TextAlignment.CENTER)

        val titleStyleME = Style()
            .setBackgroundColor(DeviceRgb(210, 157, 216))
            .setTextAlignment(TextAlignment.CENTER)

        // Create the table
        val table = Table(UnitValue.createPercentArray(floatArrayOf(1f)))
        table.setWidth(UnitValue.createPercentValue(100f))


        table.addCell(
            createCell(
                context.getString(R.string.medical_exam),
                titleStyle,
                DeviceRgb(99, 34, 105)
            )
        )
        table.addCell(context.getString(diagnosis.disease.displayNameResource))
        table.addCell(
            createCell(
                context.getString(R.string.date_time),
                titleStyle,
                DeviceRgb(99, 34, 105)
            )
        )
        table.addCell(dateStr)
        table.addCell(
            createCell(
                context.getString(R.string.patient),
                titleStyle,
                DeviceRgb(99, 34, 105)
            )
        )
        table.addCell(diagnosis.patient.name)
        table.addCell(
            createCell(
                context.getString(R.string.patient_id_document),
                titleStyle,
                DeviceRgb(99, 34, 105)
            )
        )
        table.addCell(diagnosis.patient.document)
        table.addCell(
            createCell(
                context.getString(R.string.specialist),
                titleStyle,
                DeviceRgb(99, 34, 105)
            )
        )
        table.addCell(diagnosis.specialist.name)
        table.addCell(
            createCell(
                context.getString(R.string.number_of_samples),
                titleStyle,
                DeviceRgb(99, 34, 105)
            )
        )
        table.addCell(diagnosis.images.size.toString())

        val tableModDiag = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
        tableModDiag.setWidth(UnitValue.createPercentValue(100f))

        tableModDiag.addCell(
            createCell(
                context.getString(R.string.model),
                titleStyleME,
                DeviceRgb(210, 157, 216)
            )
        )
        tableModDiag.addCell(
            createCell(
                context.getString(R.string.specialist),
                titleStyleME,
                DeviceRgb(210, 157, 216)
            )
        )
        table.addCell(tableModDiag)
        table.addCell(
            createCell(
                context.getString(R.string.tab_results),
                titleStyle,
                DeviceRgb(99, 34, 105)
            )
        )

        val table2 = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
        table2.setWidth(UnitValue.createPercentValue(100f))

        table2.addCell(diagnosis.results.modelResult.toString())
        table2.addCell(diagnosis.results.specialistResult.toString())
        table.addCell(table2)

        // TODO: Dynamically add results based on disease

        table.addCell(createCell("Parásitos ", titleStyle, DeviceRgb(99, 34, 105)))

        val table3 = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
        table3.setWidth(UnitValue.createPercentValue(100f))

        table3.addCell("6")
        table3.addCell("6")
        table.addCell(table3)

        val table4 = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
        table4.setWidth(UnitValue.createPercentValue(100f))

        table.addCell(createCell("Macrofagos", titleStyle, DeviceRgb(99, 34, 105)))

        table4.addCell("8")
        table4.addCell("8")
        table.addCell(table4)

        table.setTextAlignment(TextAlignment.CENTER)

        // Add table to document
        document.add(table)

        // Close the document
        document.close()
        return pdfFile
    }

    private fun createCell(text: String, style: Style, backgroundColor: DeviceRgb?): Cell {
        val cell = Cell()

        // Establecer el color de fondo de la celda
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor)
        }

        val paragraph = Paragraph(text).addStyle(style)
        cell.add(paragraph)
        return cell
    }
}