package com.docengine.core;

// Product type 2: puts a mark (ORIGINAL, DRAFT...) on the content, each format does it differently
public interface Watermarker<F extends Format> {
    String stamp(String content, String label);
}
