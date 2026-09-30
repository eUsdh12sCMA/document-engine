package com.docengine.pdf;

import com.docengine.core.Watermarker;

class PdfWatermarker implements Watermarker<Pdf> {
    // in pdf the watermark is an object added at the end of the page
    public String stamp(String content, String label) {
        return content + "/Watermark << /Text (" + label + ") /Opacity 0.3 >>\n";
    }
}
