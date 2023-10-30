package com.leishmaniapp.infrastructure

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
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.usecases.IDiagnosisSharing
import kotlinx.datetime.toJavaLocalDateTime
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.time.format.DateTimeFormatter
import java.util.Locale

@Suppress("UNREACHABLE_CODE")
class PDFDiagnosisSharing : IDiagnosisSharing {

    override suspend fun shareDiagnosisFile(diagnosis: Diagnosis): File {
        val pdfFile = File.createTempFile("diagnosis", ".pdf")
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

        // Crear la tabla
        val table = Table(UnitValue.createPercentArray(floatArrayOf(1f)))
        table.setWidth(UnitValue.createPercentValue(100f))

        table.addCell(createCell("Examen", titleStyle, DeviceRgb(99, 34, 105)))
        table.addCell("Leishmaniasis tincion Giemsa")

        table.addCell(createCell("Fecha y Hora", titleStyle, DeviceRgb(99, 34, 105)))
        table.addCell(dateStr)

        table.addCell(createCell("Paciente Asociado", titleStyle, DeviceRgb(99, 34, 105)))
        table.addCell(diagnosis.patient.name)

        table.addCell(createCell("Identificación del Paciente", titleStyle, DeviceRgb(99, 34, 105)))
        table.addCell(diagnosis.patient.id.value)

        table.addCell(createCell("Especialista", titleStyle, DeviceRgb(99, 34, 105)))
        table.addCell(diagnosis.specialist.name)

        table.addCell(createCell("Número de Muestras", titleStyle, DeviceRgb(99, 34, 105)))
        table.addCell(diagnosis.images.size.toString())

        val tableModDiag = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
        tableModDiag.setWidth(UnitValue.createPercentValue(100f))

        tableModDiag.addCell(createCell("Modelo", titleStyleME, DeviceRgb(210, 157, 216)))
        tableModDiag.addCell(createCell("Especialista", titleStyleME, DeviceRgb(210, 157, 216)))
        table.addCell(tableModDiag)

        table.addCell(createCell("Resultado del diagnostico", titleStyle, DeviceRgb(99, 34, 105)))

        val table2 = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
        table2.setWidth(UnitValue.createPercentValue(100f))

        table2.addCell(diagnosis.modelResult.toString())
        table2.addCell(diagnosis.specialistResult.toString())
        table.addCell(table2)

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

        // Agregar la tabla al documento
        document.add(table)

        // Cerrar el documento
        document.close()

        // SE TIENE QUE LLAMAR PARA PODER GUARDAR EL ARCHIVO Y MANDAR EL CONTEXTO
        //savePDFToDownloads(pdfFile, this)
        return pdfFile
    }

    fun createCell(text: String, style: Style, backgroundColor: DeviceRgb?): Cell {
        val cell = Cell()

        // Establecer el color de fondo de la celda
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor)
        }

        val paragraph = Paragraph(text).addStyle(style)
        cell.add(paragraph)
        return cell
    }

    fun savePDFToDownloads(pdfFile: File, context: Context) {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destinationFile = File(downloadsDir, "Diagnostic.pdf")

        try {
            FileInputStream(pdfFile).use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
            // Notificar al sistema que se ha agregado un archivo
            MediaScannerConnection.scanFile(
                context,
                arrayOf(destinationFile.absolutePath),
                null,
                null
            )
            //Toast.makeText(this, "PDF guardado en Descargas", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            //Toast.makeText(this, "Error al guardar el PDF", Toast.LENGTH_SHORT).show()
        }
    }
}