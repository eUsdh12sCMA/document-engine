package com.docengine.pdf;

import com.docengine.core.*;

// the only public way to get Pdf products. All three come from the same package, so they match.
public class PdfFactory implements DocumentFactory<Pdf> {
    public Renderer<Pdf> createRenderer() { return new PdfRenderer(); }
    public Watermarker<Pdf> createWatermarker() { return new PdfWatermarker(); }
    public Exporter<Pdf> createExporter() { return new PdfExporter(); }
}
