package com.docengine.latex;

import com.docengine.core.*;

// the only public way to get Latex products. All three come from the same package, so they match.
public class LatexFactory implements DocumentFactory<Latex> {
    public Renderer<Latex> createRenderer() { return new LatexRenderer(); }
    public Watermarker<Latex> createWatermarker() { return new LatexWatermarker(); }
    public Exporter<Latex> createExporter() { return new LatexExporter(); }
}
