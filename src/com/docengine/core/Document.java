package com.docengine.core;

// FACTORY METHOD product: the thing we want to generate (invoice, report, contract)
public interface Document {
    String baseName();
    String stampLabel();

    // the document lays itself out, but only through the Renderer of the chosen family
    <F extends Format> String content(Renderer<F> renderer);
}
