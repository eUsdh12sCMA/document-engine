package com.docengine.markdown;

import com.docengine.core.Watermarker;

class MarkdownWatermarker implements Watermarker<Markdown> {
    // markdown has no real watermark, so a quote block on top is the closest thing
    public String stamp(String content, String label) { return "> **" + label + "**\n\n" + content; }
}
