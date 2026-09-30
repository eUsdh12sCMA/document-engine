package com.docengine.markdown;

import com.docengine.core.Exporter;

class MarkdownExporter implements Exporter<Markdown> {
    public String extension() { return ".md"; }
    public String wrap(String content) { return content; } // markdown is already a plain file
}
