package com.docengine.pdf;

import com.docengine.core.Exporter;

class PdfExporter implements Exporter<Pdf> {
    public String extension() { return ".pdf"; }
    public String wrap(String content) { return "%PDF-1.7\n" + content + "%%EOF"; }
}
