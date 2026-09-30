package com.docengine.pdf;

import com.docengine.core.Renderer;

// package-private, so nobody outside can build it by hand
class PdfRenderer implements Renderer<Pdf> {
    public String heading(String text) { return "BT /F1 24 Tf (" + text + ") Tj ET\n"; }
    public String paragraph(String text) { return "BT /F1 12 Tf (" + text + ") Tj ET\n"; }
    public String row(String label, String value) { return "BT /F1 12 Tf (" + label + ": " + value + ") Tj ET\n"; }
}
