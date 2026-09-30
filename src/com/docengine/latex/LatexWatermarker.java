package com.docengine.latex;

import com.docengine.core.Watermarker;

class LatexWatermarker implements Watermarker<Latex> {
    public String stamp(String content, String label) { return "\\watermark{" + label + "}\n" + content; }
}
