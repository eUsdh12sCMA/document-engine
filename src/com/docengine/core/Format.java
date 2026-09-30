package com.docengine.core;

// Marker only, no methods. It "tags" every product with its family at compile time,
// so Renderer<Pdf> and Exporter<Html> are different types and can not be mixed.
public interface Format {}
