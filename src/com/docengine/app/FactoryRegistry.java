package com.docengine.app;

import com.docengine.core.DocumentFactory;
import com.docengine.html.HtmlFactory;
import com.docengine.latex.LatexFactory;
import com.docengine.markdown.MarkdownFactory;
import com.docengine.pdf.PdfFactory;
import java.util.*;
import java.util.function.Supplier;

// Runtime selection: name (string) -> factory. This is the only class that knows concrete factories.
public final class FactoryRegistry {
    private static final Map<String, Supplier<DocumentFactory<?>>> FAMILIES = new LinkedHashMap<>();

    static {
        FAMILIES.put("pdf", PdfFactory::new);
        FAMILIES.put("html", HtmlFactory::new);
        FAMILIES.put("markdown", MarkdownFactory::new);
        FAMILIES.put("latex", LatexFactory::new); // family D, added in the last commit
    }

    private FactoryRegistry() {}

    public static DocumentFactory<?> create(String family) {
        Supplier<DocumentFactory<?>> supplier = FAMILIES.get(family.toLowerCase());
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown family '" + family + "'. Available: " + FAMILIES.keySet());
        }
        return supplier.get();
    }

    public static Set<String> families() { return Collections.unmodifiableSet(FAMILIES.keySet()); }
}
