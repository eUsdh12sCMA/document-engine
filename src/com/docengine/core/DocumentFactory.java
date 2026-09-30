package com.docengine.core;

// ABSTRACT FACTORY: one factory = one whole family (pdf, html...).
// The type parameter F is the guarantee: all three products always have the SAME F,
// so a client can not take a PDF renderer and an HTML exporter together (compile error).
public interface DocumentFactory<F extends Format> {
    Renderer<F> createRenderer();
    Watermarker<F> createWatermarker();
    Exporter<F> createExporter();
}
