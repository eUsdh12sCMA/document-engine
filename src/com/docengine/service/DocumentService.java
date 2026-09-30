package com.docengine.service;

import com.docengine.core.*;
import com.docengine.documents.*;
import java.util.List;

// Business layer. It only knows the abstractions from core.
// It never sees Pdf, Html... so it works the same for any family (also for future ones).
// Creators depend on the document type, not on the family, so creating them here is ok.
public class DocumentService<F extends Format> {
    private final DocumentFactory<F> factory;
    private final DocumentCreator invoices = new InvoiceCreator();
    private final DocumentCreator reports = new ReportCreator();
    private final DocumentCreator contracts = new ContractCreator();

    public DocumentService(DocumentFactory<F> factory) { this.factory = factory; }

    // operation 1: invoice = Invoice + Renderer + Watermarker(ORIGINAL) + Exporter
    public ExportedFile issueInvoice(String customer, String amount) {
        return invoices.generate(factory, customer, amount);
    }

    // operation 2: internal report, stamped CONFIDENTIAL
    public ExportedFile archiveReport(String title, String summary) {
        return reports.generate(factory, title, summary);
    }

    // operation 3: contract with the partner, stamped LEGAL COPY
    public ExportedFile signContract(String company, String partner) {
        return contracts.generate(factory, company, partner);
    }

    // operation 4: month closing = invoice and report together, both in the same family
    public List<ExportedFile> closeMonth(String customer, String amount, String summary) {
        return List.of(issueInvoice(customer, amount), archiveReport("Monthly " + customer, summary));
    }

    // operation 5: quick draft for the manager, nothing is exported
    public String previewContract(String company, String partner) {
        return contracts.preview(factory, company, partner);
    }
}
