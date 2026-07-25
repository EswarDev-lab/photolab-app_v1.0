package com.photolab.management.utils.printing

import android.content.Context
import androidx.core.content.FileProvider
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.photolab.management.data.database.entity.CompanySettingsEntity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ReceiptLineItem(val description: String, val quantity: Double, val rate: Double, val total: Double)

data class ReceiptData(
    val invoiceNumber: String,
    val date: Long = System.currentTimeMillis(),
    val customerName: String,
    val customerPhone: String,
    val items: List<ReceiptLineItem>,
    val discount: Double = 0.0,
    val gstAmount: Double = 0.0,
    val advancePaid: Double = 0.0,
    val totalAmount: Double
)

/**
 * Generates a printable PDF receipt/invoice and returns a shareable content:// Uri via
 * FileProvider — hand that Uri to an ACTION_SEND or ACTION_VIEW intent to open a printer
 * dialog, share to WhatsApp, etc.
 */
object ReceiptPdfGenerator {

    fun generate(context: Context, company: CompanySettingsEntity, receipt: ReceiptData): android.net.Uri {
        val outputDir = File(context.cacheDir, "receipts").apply { mkdirs() }
        val outputFile = File(outputDir, "${receipt.invoiceNumber}.pdf")

        PdfWriter(outputFile).use { writer ->
            PdfDocument(writer).use { pdfDoc ->
                Document(pdfDoc, PageSize.A4).use { doc ->
                    doc.setMargins(36f, 36f, 36f, 36f)

                    doc.add(Paragraph(company.companyName).setBold().setFontSize(18f))
                    company.address?.let { doc.add(Paragraph(it).setFontSize(9f)) }
                    val contactLine = listOfNotNull(company.phone, company.email).joinToString(" · ")
                    if (contactLine.isNotBlank()) doc.add(Paragraph(contactLine).setFontSize(9f))
                    company.gstNumber?.let { doc.add(Paragraph("GSTIN: $it").setFontSize(9f)) }

                    doc.add(Paragraph("\n"))
                    doc.add(Paragraph("INVOICE").setBold().setFontSize(14f))

                    val headerTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f))).useAllAvailableWidth()
                    headerTable.addCell(borderlessCell("Invoice #: ${receipt.invoiceNumber}"))
                    headerTable.addCell(
                        borderlessCell("Date: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(receipt.date))}")
                            .setTextAlignment(TextAlignment.RIGHT)
                    )
                    headerTable.addCell(borderlessCell("Customer: ${receipt.customerName}"))
                    headerTable.addCell(borderlessCell("Phone: ${receipt.customerPhone}").setTextAlignment(TextAlignment.RIGHT))
                    doc.add(headerTable)

                    doc.add(Paragraph("\n"))

                    val itemsTable = Table(UnitValue.createPercentArray(floatArrayOf(4f, 1f, 1.5f, 1.5f))).useAllAvailableWidth()
                    listOf("Description", "Qty", "Rate", "Total").forEach {
                        itemsTable.addHeaderCell(Cell().add(Paragraph(it).setBold()))
                    }
                    receipt.items.forEach { item ->
                        itemsTable.addCell(Paragraph(item.description))
                        itemsTable.addCell(Paragraph(item.quantity.toString()).setTextAlignment(TextAlignment.RIGHT))
                        itemsTable.addCell(Paragraph("%.2f".format(item.rate)).setTextAlignment(TextAlignment.RIGHT))
                        itemsTable.addCell(Paragraph("%.2f".format(item.total)).setTextAlignment(TextAlignment.RIGHT))
                    }
                    doc.add(itemsTable)

                    doc.add(Paragraph("\n"))
                    val subtotal = receipt.items.sumOf { it.total }
                    val summaryTable = Table(UnitValue.createPercentArray(floatArrayOf(3f, 1f))).useAllAvailableWidth()
                    summaryTable.addCell(borderlessCell("Subtotal").setTextAlignment(TextAlignment.RIGHT))
                    summaryTable.addCell(borderlessCell("%.2f".format(subtotal)).setTextAlignment(TextAlignment.RIGHT))
                    if (receipt.discount > 0) {
                        summaryTable.addCell(borderlessCell("Discount").setTextAlignment(TextAlignment.RIGHT))
                        summaryTable.addCell(borderlessCell("-%.2f".format(receipt.discount)).setTextAlignment(TextAlignment.RIGHT))
                    }
                    if (receipt.gstAmount > 0) {
                        summaryTable.addCell(borderlessCell("GST").setTextAlignment(TextAlignment.RIGHT))
                        summaryTable.addCell(borderlessCell("%.2f".format(receipt.gstAmount)).setTextAlignment(TextAlignment.RIGHT))
                    }
                    summaryTable.addCell(borderlessCell("Total").setBold().setTextAlignment(TextAlignment.RIGHT))
                    summaryTable.addCell(borderlessCell("${company.currencySymbol}%.2f".format(receipt.totalAmount)).setBold().setTextAlignment(TextAlignment.RIGHT))
                    if (receipt.advancePaid > 0) {
                        val balance = receipt.totalAmount - receipt.advancePaid
                        summaryTable.addCell(borderlessCell("Advance Paid").setTextAlignment(TextAlignment.RIGHT))
                        summaryTable.addCell(borderlessCell("%.2f".format(receipt.advancePaid)).setTextAlignment(TextAlignment.RIGHT))
                        summaryTable.addCell(borderlessCell("Balance Due").setBold().setTextAlignment(TextAlignment.RIGHT))
                        summaryTable.addCell(borderlessCell("%.2f".format(balance)).setBold().setTextAlignment(TextAlignment.RIGHT))
                    }
                    doc.add(summaryTable)

                    company.receiptFooter?.let {
                        doc.add(Paragraph("\n"))
                        doc.add(Paragraph(it).setItalic().setFontSize(9f).setTextAlignment(TextAlignment.CENTER))
                    }
                }
            }
        }

        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", outputFile)
    }

    private fun borderlessCell(text: String): Cell =
        Cell().add(Paragraph(text)).setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
}
