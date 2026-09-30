package com.docengine.app;

import com.docengine.core.DocumentFactory;
import com.docengine.core.ExportedFile;
import com.docengine.service.DocumentService;

// The client. It imports no family package, only core + service.
// Run: java -cp out com.docengine.app.Main family=html   (or set DOC_FAMILY=html)
public class Main {
    public static void main(String[] args) {
        String family = FamilySelector.choose(args);
        DocumentFactory<?> factory = FactoryRegistry.create(family);
        DocumentService<?> service = new DocumentService<>(factory);

        System.out.println("=== Family: " + family + " ===");
        print(service.issueInvoice("Acme Ltd", "1200"));
        print(service.archiveReport("Q3 Sales", "Sales grew by 8 percent."));
        print(service.signContract("Our Company", "Beta LLC"));
        service.closeMonth("Acme Ltd", "300", "Month closed without problems.").forEach(Main::print);
        System.out.println("--- draft preview ---\n" + service.previewContract("Our Company", "Beta LLC"));

        // This does NOT compile, the design blocks it (Pdf renderer + Html exporter):
        // Renderer<Pdf> r = new HtmlFactory().createRenderer();
    }

    private static void print(ExportedFile file) {
        System.out.println("--- " + file.name() + " ---\n" + file.body() + "\n");
    }
}
