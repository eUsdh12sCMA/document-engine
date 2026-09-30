package com.docengine.service;

import static org.junit.jupiter.api.Assertions.*;

import com.docengine.core.ExportedFile;
import com.docengine.html.HtmlFactory;
import com.docengine.markdown.MarkdownFactory;
import com.docengine.pdf.PdfFactory;
import java.util.List;
import org.junit.jupiter.api.Test;

// PART F: business operations where several products work together
class BusinessOperationsTest {

    @Test
    void invoiceIsBuiltStampedOriginalAndExportedInTheChosenFormat() {
        var service = new DocumentService<>(new HtmlFactory());

        ExportedFile invoice = service.issueInvoice("Acme Ltd", "1200");

        assertEquals("invoice-acme-ltd.html", invoice.name());                 // Exporter
        assertTrue(invoice.body().startsWith("<!DOCTYPE html>"));               // Exporter
        assertTrue(invoice.body().contains("<h1>Invoice for Acme Ltd</h1>"));   // Renderer
        assertTrue(invoice.body().contains("<b>Total due</b>: 1200 USD"));      // Renderer
        assertTrue(invoice.body().contains("<div class=\"watermark\">ORIGINAL</div>")); // Watermarker
        assertTrue(invoice.body().endsWith("</body></html>"));
        // nothing from another family
        assertFalse(invoice.body().contains("BT /F1"));
        assertFalse(invoice.body().contains("%PDF"));
    }

    @Test
    void closingTheMonthCreatesInvoiceAndConfidentialReportInTheSameFamily() {
        var service = new DocumentService<>(new MarkdownFactory());

        List<ExportedFile> files = service.closeMonth("Acme Ltd", "300", "Month closed without problems.");

        assertEquals(2, files.size());
        ExportedFile invoice = files.get(0);
        ExportedFile report = files.get(1);
        assertEquals("invoice-acme-ltd.md", invoice.name());
        assertEquals("report-monthly-acme-ltd.md", report.name()); // same family, same extension
        assertTrue(invoice.body().startsWith("> **ORIGINAL**"));
        assertTrue(invoice.body().contains("**Total due**: 300 USD"));
        assertTrue(report.body().startsWith("> **CONFIDENTIAL**"));
        assertTrue(report.body().contains("Month closed without problems."));
    }

    @Test
    void contractPreviewIsAlwaysDraftAndNotExportedWhileSigningGivesTheLegalCopy() {
        var service = new DocumentService<>(new PdfFactory());

        String preview = service.previewContract("Our Company", "Beta LLC");
        ExportedFile signed = service.signContract("Our Company", "Beta LLC");

        // preview: DRAFT stamp, and no export wrapper (it is not a file)
        assertTrue(preview.contains("/Text (DRAFT)"));
        assertFalse(preview.contains("LEGAL COPY"));
        assertFalse(preview.startsWith("%PDF"));
        // signed contract: legal stamp and a complete pdf file
        assertEquals("contract-beta-llc.pdf", signed.name());
        assertTrue(signed.body().startsWith("%PDF-1.7"));
        assertTrue(signed.body().endsWith("%%EOF"));
        assertTrue(signed.body().contains("/Text (LEGAL COPY)"));
        assertTrue(signed.body().contains("(Signed by: Our Company / Beta LLC)"));
        assertFalse(signed.body().contains("DRAFT"));
    }

    @Test
    void emptyOrMissingDocumentDataIsRejectedBeforeAnyDocumentIsBuilt() {
        var service = new DocumentService<>(new PdfFactory());

        assertThrows(IllegalArgumentException.class, () -> service.issueInvoice("   ", "1200"));
        assertThrows(IllegalArgumentException.class, () -> service.archiveReport("Q3 Sales", null));
        IllegalArgumentException error =
                assertThrows(IllegalArgumentException.class, () -> service.previewContract("", "Beta LLC"));
        assertEquals("Document data must not be empty", error.getMessage());
    }
}