package com.docengine.service;

import static org.junit.jupiter.api.Assertions.*;

import com.docengine.app.FactoryRegistry;
import com.docengine.app.FamilySelector;
import com.docengine.app.Main;
import com.docengine.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

// Proof that the client works through abstractions only
class ClientAbstractionTest {

    // A family that the production code has never seen: plain text in capital letters.
    private static final class Shout implements Format {}

    private static final class ShoutFactory implements DocumentFactory<Shout> {
        public Renderer<Shout> createRenderer() {
            return new Renderer<Shout>() {
                public String heading(String text) { return text.toUpperCase() + "\n"; }
                public String paragraph(String text) { return text + "\n"; }
                public String row(String label, String value) { return label + " = " + value + "\n"; }
            };
        }

        public Watermarker<Shout> createWatermarker() {
            return (content, label) -> "[" + label + "] " + content;
        }

        public Exporter<Shout> createExporter() {
            return new Exporter<Shout>() {
                public String extension() { return ".shout"; }
                public String wrap(String content) { return content; }
            };
        }
    }

    @Test
    void serviceWorksWithAFamilyItHasNeverSeenWithoutAnyChange() {
        var service = new DocumentService<>(new ShoutFactory());

        ExportedFile invoice = service.issueInvoice("Acme Ltd", "1200");
        List<ExportedFile> month = service.closeMonth("Acme Ltd", "300", "Fine.");

        assertEquals("invoice-acme-ltd.shout", invoice.name());
        assertTrue(invoice.body().startsWith("[ORIGINAL] INVOICE FOR ACME LTD"));
        assertTrue(invoice.body().contains("Total due = 1200 USD"));
        assertEquals(2, month.size());
        assertTrue(month.get(1).body().startsWith("[CONFIDENTIAL] MONTHLY ACME LTD"));
    }

    @Test
    void clientAndBusinessClassesDoNotReferenceAnyConcreteFamilyPackage() throws IOException {
        List<String> familyPackages =
                List.of("com/docengine/pdf/", "com/docengine/html/", "com/docengine/markdown/", "com/docengine/latex/");

        // we read the compiled class files: every class that is used is written there by name
        for (Class<?> client : List.of(Main.class, FamilySelector.class, DocumentService.class, DocumentCreator.class)) {
            String bytecode = bytecodeOf(client);
            for (String familyPackage : familyPackages) {
                assertFalse(bytecode.contains(familyPackage),
                        client.getSimpleName() + " must not use " + familyPackage);
            }
        }

        // control: the registry is the one place that knows the concrete factories
        String registry = bytecodeOf(FactoryRegistry.class);
        assertTrue(registry.contains("com/docengine/pdf/PdfFactory"));
        assertTrue(registry.contains("com/docengine/html/HtmlFactory"));
    }

    private static String bytecodeOf(Class<?> type) throws IOException {
        String resource = "/" + type.getName().replace('.', '/') + ".class";
        try (InputStream in = type.getResourceAsStream(resource)) {
            assertNotNull(in, "class file not found: " + resource);
            return new String(in.readAllBytes(), StandardCharsets.ISO_8859_1);
        }
    }
}