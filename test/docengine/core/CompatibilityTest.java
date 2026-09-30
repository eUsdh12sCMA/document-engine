package com.docengine.core;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.tools.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

// PART D: the compatibility rule is checked by the real Java compiler.
// We compile small "client" snippets against our classes and look at the result.
class CompatibilityTest {

    // javac error code for "incompatible types" (does not depend on the language of the JDK)
    private static final String INCOMPATIBLE_TYPES = "compiler.err.prob.found.req";

    private record Result(boolean compiled, List<String> errorCodes, String messages) {}

    private static Result compile(Path workDir, String statements) throws Exception {
        String source = """
                import com.docengine.core.*;
                import com.docengine.pdf.*;
                import com.docengine.html.*;
                import com.docengine.markdown.*;
                public class Probe {
                    void run() {
                        %s
                    }
                }
                """.formatted(statements);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull(compiler, "These tests need a JDK (not only a JRE)");

        JavaFileObject file = new SimpleJavaFileObject(URI.create("string:///Probe.java"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) { return source; }
        };
        // where our compiled classes live (target/classes, out/, ...)
        String classes = Path.of(DocumentFactory.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        boolean ok = compiler.getTask(null, null, diagnostics,
                List.of("-classpath", classes, "-d", workDir.toString()), null, List.of(file)).call();

        List<Diagnostic<? extends JavaFileObject>> errors = diagnostics.getDiagnostics().stream()
                .filter(d -> d.getKind() == Diagnostic.Kind.ERROR).collect(Collectors.toList());
        return new Result(ok,
                errors.stream().map(Diagnostic::getCode).collect(Collectors.toList()),
                errors.stream().map(d -> d.getMessage(Locale.ENGLISH)).collect(Collectors.joining("\n")));
    }

    @Test
    void productsOfDifferentFamiliesCanNotBeCombinedBecauseTheCodeDoesNotCompile(@TempDir Path workDir) throws Exception {
        // control: three products of ONE family compile, so a failure below is caused by the mixing
        Result sameFamily = compile(workDir, """
                DocumentFactory<Pdf> factory = new PdfFactory();
                Renderer<Pdf> renderer = factory.createRenderer();
                Watermarker<Pdf> watermarker = factory.createWatermarker();
                Exporter<Pdf> exporter = factory.createExporter();
                """);
        assertTrue(sameFamily.compiled(), sameFamily.messages());

        // Pdf renderer taken from the Html factory
        Result wrongRenderer = compile(workDir, "Renderer<Pdf> renderer = new HtmlFactory().createRenderer();");
        assertFalse(wrongRenderer.compiled());
        assertTrue(wrongRenderer.errorCodes().contains(INCOMPATIBLE_TYPES), wrongRenderer.messages());

        // Pdf exporter taken from the Markdown factory
        Result wrongExporter = compile(workDir, "Exporter<Pdf> exporter = new MarkdownFactory().createExporter();");
        assertFalse(wrongExporter.compiled());
        assertTrue(wrongExporter.errorCodes().contains(INCOMPATIBLE_TYPES), wrongExporter.messages());

        // a Pdf factory can not pretend to be an Html factory
        Result wrongFactory = compile(workDir, "DocumentFactory<Html> factory = new PdfFactory();");
        assertFalse(wrongFactory.compiled());
        assertTrue(wrongFactory.errorCodes().contains(INCOMPATIBLE_TYPES), wrongFactory.messages());
    }
}