package com.docengine.app;

import static org.junit.jupiter.api.Assertions.*;

import com.docengine.core.DocumentFactory;
import com.docengine.core.ExportedFile;
import com.docengine.html.HtmlFactory;
import com.docengine.markdown.MarkdownFactory;
import com.docengine.pdf.PdfFactory;
import com.docengine.service.DocumentService;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

// PART E: the family is chosen at runtime, from outside
class RuntimeSelectionTest {

    @Test
    void theNameOfTheFamilyDecidesWhichFactoryAndWhichFileFormatIsUsed() {
        assertInstanceOf(PdfFactory.class, FactoryRegistry.create("pdf"));
        assertInstanceOf(HtmlFactory.class, FactoryRegistry.create("html"));
        assertInstanceOf(MarkdownFactory.class, FactoryRegistry.create("markdown"));
        assertInstanceOf(HtmlFactory.class, FactoryRegistry.create("HTML")); // not case sensitive
        assertTrue(FactoryRegistry.families().containsAll(Set.of("pdf", "html", "markdown")));

        // the same business call gives a different file, only because of the name
        Map<String, String> extensionByFamily = Map.of("pdf", ".pdf", "html", ".html", "markdown", ".md");
        extensionByFamily.forEach((family, extension) -> {
            DocumentFactory<?> factory = FactoryRegistry.create(family);
            ExportedFile invoice = new DocumentService<>(factory).issueInvoice("Acme Ltd", "1200");
            assertEquals("invoice-acme-ltd" + extension, invoice.name());
        });
    }

    @Test
    void selectorUsesCommandLineFirstThenEnvironmentThenDefault() {
        Map<String, String> env = Map.of("DOC_FAMILY", "markdown");

        assertEquals("html", FamilySelector.choose(new String[] {"family=html"}, env)); // command line wins
        assertEquals("markdown", FamilySelector.choose(new String[] {}, env));           // then environment
        assertEquals(FamilySelector.DEFAULT, FamilySelector.choose(new String[] {}, Map.of())); // then default
        // other arguments are ignored
        assertEquals("html", FamilySelector.choose(new String[] {"verbose", "family=html"}, Map.of()));
    }

    @Test
    void unknownFamilyIsRejectedAndTheMessageListsTheAvailableOnes() {
        IllegalArgumentException error =
                assertThrows(IllegalArgumentException.class, () -> FactoryRegistry.create("quantum"));

        assertTrue(error.getMessage().contains("quantum"));
        assertTrue(error.getMessage().contains("pdf"));
        assertTrue(error.getMessage().contains("html"));
    }
}