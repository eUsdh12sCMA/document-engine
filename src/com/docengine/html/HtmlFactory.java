package com.docengine.html;

import com.docengine.core.*;

// the only public way to get Html products. All three come from the same package, so they match.
public class HtmlFactory implements DocumentFactory<Html> {
    public Renderer<Html> createRenderer() { return new HtmlRenderer(); }
    public Watermarker<Html> createWatermarker() { return new HtmlWatermarker(); }
    public Exporter<Html> createExporter() { return new HtmlExporter(); }
}
