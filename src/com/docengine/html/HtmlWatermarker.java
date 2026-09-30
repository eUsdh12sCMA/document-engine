package com.docengine.html;

import com.docengine.core.Watermarker;

class HtmlWatermarker implements Watermarker<Html> {
    // in html the mark is a div at the top
    public String stamp(String content, String label) {
        return "<div class=\"watermark\">" + label + "</div>\n" + content;
    }
}
