package com.leishmaniapp.infrastructure.service.sharing

import android.content.Context
import android.icu.number.NumberFormatter.UnitWidth
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.barcodes.qrcode.QRCodeWriter
import com.itextpdf.io.font.FontProgramFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.font.FontProvider
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.services.IDiagnosisSharingService
import com.leishmaniapp.utilities.extensions.utcNow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import java.io.File
import javax.inject.Inject

/**
 * Share a [Diagnosis] into PDF format
 */
class ApplicationDiagnosisSharingServiceImpl @Inject constructor(
    @ApplicationContext val context: Context,
) : IDiagnosisSharingService {

    /**
     * MIME type for resulting file
     */
    override val mime: String = "application/pdf"

    private fun applyCustomTableStyle(table: Table) = table.apply {
        setWidth(UnitValue.createPercentValue(100f))
    }

    private fun applyCustomHeaderCellStyle(cell: Cell) = cell.apply {
        setFontColor(ColorConstants.WHITE)
        setBackgroundColor(ColorConstants.DARK_GRAY)
        setTextAlignment(TextAlignment.CENTER)
        setBold()
    }

    private fun applyCustomSubHeaderCellStyle(cell: Cell) = cell.apply {
        setBackgroundColor(ColorConstants.GRAY)
        setTextAlignment(TextAlignment.CENTER)
    }

    private fun applyCustomSubHeaderSupportCellStyle(cell: Cell) = cell.apply {
        setBackgroundColor(ColorConstants.LIGHT_GRAY)
        setTextAlignment(TextAlignment.CENTER)
        setItalic()
    }

    override suspend fun share(diagnosis: Diagnosis): File = withContext(Dispatchers.IO) {

        // Create the PDF file
        val pdfFile = withContext(Dispatchers.IO) {
            File.createTempFile(diagnosis.id.toString(), ".pdf")
        }

        // Generate PDF into file
        val pdfDocument = PdfDocument(PdfWriter(pdfFile))

        // Create the auxiliary font
        val adaminaFont = context.resources.openRawResource(+R.font.adamina_regular).use {
            PdfFontFactory.createFont(FontProgramFactory.createFont(it.readBytes()))
        }

        // Build the document
        Document(pdfDocument).use { document ->

            // Header
            Table(UnitValue.createPercentArray(floatArrayOf(1.0f, 0.3f))).apply {
                setWidth(UnitValue.createPercentValue(100f))

                addCell(Paragraph().apply {
                    // Title
                    add(Text(context.getString(R.string.document_title) + "\n").apply {
                        setFont(adaminaFont)
                        setMultipliedLeading(1.0f)
                        setFontSize(20f)
                    })
                    // Subtitle
                    add(Text(context.getString(R.string.document_subtitle) + "\n").apply {
                        setFontSize(16f)
                        setItalic()
                    })
                    // Diagnosis UUID
                    add(Text("\n" + context.getString(R.string.document_code) + "\n"))
                    add(Text(diagnosis.id.toString()).apply {
                        setItalic()
                    })
                })
                addCell(
                    Image(
                        BarcodeQRCode(diagnosis.id.toString())
                            .createFormXObject(pdfDocument)
                    ).apply {
                        setAutoScale(true)
                    }
                )

                // Add the table to the document
                applyCustomTableStyle(this)
                document.add(this)
            }

            // Basic information
            Table(UnitValue.createPercentArray(floatArrayOf(1.0f, 0.5f))).apply {

                addHeaderCell(Cell(1, 2).apply {
                    add(Paragraph(context.getString(R.string.document_base)))
                    applyCustomHeaderCellStyle(this)
                })

                addCell(Cell().apply {
                    add(Paragraph(context.getString(R.string.document_base_disease)))
                    applyCustomSubHeaderCellStyle(this)
                })
                addCell(Cell().apply {
                    add(Paragraph(context.getString(R.string.document_base_disease_id)))
                    applyCustomSubHeaderCellStyle(this)
                })

                diagnosis.disease.let { disease ->
                    addCell(Paragraph(context.getString(disease.displayNameResource)))
                    addCell(Paragraph(disease.id))
                }

                addCell(Cell().apply {
                    add(Paragraph(context.getString(R.string.document_base_date)))
                    applyCustomSubHeaderCellStyle(this)
                })
                addCell(Cell().apply {
                    add(Paragraph(context.getString(R.string.document_base_sample)))
                    applyCustomSubHeaderCellStyle(this)
                })

                diagnosis.let {
                    addCell(Paragraph(it.date.format(LocalDateTime.Formats.ISO)))
                    addCell(Paragraph(it.samples.toString()))
                }

                // Add the table to the document
                applyCustomTableStyle(this)
                document.add(this)
            }

            // Specialist information
            Table(UnitValue.createPercentArray(floatArrayOf(1.0f, 1.0f))).apply {
                addHeaderCell(
                    Cell(1, 2).apply {
                        add(Paragraph(context.getString(R.string.document_specialist)))
                        applyCustomHeaderCellStyle(this)
                    })

                addHeaderCell(Cell().apply {
                    add(Paragraph(context.getString(R.string.document_specialist_name)))
                    applyCustomSubHeaderCellStyle(this)
                })
                addHeaderCell(Cell().apply {
                    add(Paragraph(context.getString(R.string.document_specialist_email)))
                    applyCustomSubHeaderCellStyle(this)
                })

                diagnosis.specialist.let { specialist ->
                    addCell(Paragraph(specialist.name))
                    addCell(Paragraph(specialist.email))
                }

                // Add the table to the document
                applyCustomTableStyle(this)
                document.add(this)
            }

            // Patient information
            Table(UnitValue.createPercentArray(floatArrayOf(1.0f, 0.5f, 0.5f))).apply {

                addHeaderCell(
                    Cell(1, 3).apply {
                        add(Paragraph(context.getString(R.string.document_patient)))
                        applyCustomHeaderCellStyle(this)
                    })

                addHeaderCell(Cell().apply {
                    add(Paragraph(context.getString(R.string.document_patient_name)))
                    applyCustomSubHeaderCellStyle(this)
                })
                addHeaderCell(Cell().apply {
                    add(Paragraph(context.getString(R.string.document_patient_document_type)))
                    applyCustomSubHeaderCellStyle(this)
                })
                addHeaderCell(Cell().apply {
                    add(Paragraph(context.getString(R.string.document_patient_document_id)))
                    applyCustomSubHeaderCellStyle(this)
                })

                diagnosis.patient.let { patient ->
                    addCell(patient.name)
                    addCell(patient.documentType.name)
                    addCell(patient.id)
                }

                // Add the table to the document
                applyCustomTableStyle(this)
                document.add(this)
            }

            // Results
            Table(UnitValue.createPercentArray(floatArrayOf(1.0f, 1.0f))).apply {

                // Add header
                addHeaderCell(
                    Cell(1, 2).apply {
                        add(Paragraph(context.getString(R.string.document_results)))
                        applyCustomHeaderCellStyle(this)
                    })

                // Model and Specialist support headers
                addCell(Cell().apply {
                    add(Paragraph(context.getString(R.string.document_results_model)))
                    applyCustomSubHeaderSupportCellStyle(this)
                })
                addCell(Cell().apply {
                    add(Paragraph(context.getString(R.string.document_results_specialist)))
                    applyCustomSubHeaderSupportCellStyle(this)
                })

                // Results sub-header
                addCell(
                    Cell(1, 2).apply {
                        add(Paragraph(context.getString(R.string.document_results_result)))
                        applyCustomSubHeaderCellStyle(this)
                    })

                diagnosis.results.let { results ->
                    // Positive or Negative result cells
                    addCell(
                        Paragraph(
                            context.getString(
                                if (results.modelResult) {
                                    R.string.diagnosis_results_positive
                                } else {
                                    R.string.diagnosis_results_negative
                                }
                            )
                        )
                    )
                    addCell(
                        Paragraph(
                            context.getString(
                                if (results.specialistResult) {
                                    R.string.diagnosis_results_positive
                                } else {
                                    R.string.diagnosis_results_negative
                                }
                            )
                        )
                    )

                    // Header for elements
                    addCell(
                        Cell(1, 2).apply {
                            add(Paragraph(context.getString(R.string.document_results_elements)))
                            applyCustomSubHeaderCellStyle(this)
                        })

                    // Add each element
                    diagnosis.disease.elements.forEach { element ->
                        // Add element name support header
                        addCell(
                            Cell(1, 2).apply {
                                add(Paragraph(context.getString(element.displayNameResource)))
                                applyCustomSubHeaderSupportCellStyle(this)
                            })

                        // Find element for model
                        addCell(Paragraph(results.modelElements[element]?.toString() ?: "-"))
                        // Find element for specialist
                        addCell(Paragraph(results.specialistElements[element]?.toString() ?: "-"))
                    }
                }

                // Results sub-header
                addCell(
                    Cell(1, 2).apply {
                        add(Paragraph(context.getString(R.string.document_results_remarks)))
                        applyCustomSubHeaderCellStyle(this)
                    })

                addCell(
                    Cell(1, 2).apply {
                        add(
                            Paragraph(
                                diagnosis.remarks
                                    ?: context.getString(R.string.document_results_no_remarks)
                            )
                        )
                    })

                // Add the table to the document
                applyCustomTableStyle(this)
                document.add(this)
            }

            // Footer
            Paragraph().apply {
                add(Text(context.getString(R.string.document_footer_generation) + "\n"))
                add(
                    Text(
                        context.getString(
                            R.string.document_footer_time,
                            LocalDateTime.utcNow().format(LocalDateTime.Formats.ISO)
                        ) + "\n"
                    )
                )
                add(Text("\n" + context.getString(R.string.document_footer_disclaimer)))
                document.add(this)
            }
        }

        // Close the document
        return@withContext pdfFile
    }
}