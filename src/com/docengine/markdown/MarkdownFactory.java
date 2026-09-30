package com.docengine.markdown;

import com.docengine.core.*;

// the only public way to get Markdown products. All three come from the same package, so they match.
public class MarkdownFactory implements DocumentFactory<Markdown> {
    public Renderer<Markdown> createRenderer() { return new MarkdownRenderer(); }
    public Watermarker<Markdown> createWatermarker() { return new MarkdownWatermarker(); }
    public Exporter<Markdown> createExporter() { return new MarkdownExporter(); }
}
