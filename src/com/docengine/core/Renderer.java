package com.docengine.core;

// Product type 1: writes text in the syntax of one format
public interface Renderer<F extends Format> {
    String heading(String text);
    String paragraph(String text);
    String row(String label, String value);
}
