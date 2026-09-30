package com.docengine.core;

// Product type 3: turns the content into a complete file of one format
public interface Exporter<F extends Format> {
    String extension();
    String wrap(String content);

    // same for every format, so I keep it here as default
    default ExportedFile export(String baseName, String content) {
        return new ExportedFile(baseName + extension(), wrap(content));
    }
}
