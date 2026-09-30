package com.docengine.legacy;

// PART A: first version, NO factories. I keep it only to show the problems.
public class LegacyClient {

    interface Renderer { String heading(String text); }
    interface Watermarker { String stamp(String content); }
    interface Exporter { String export(String content); }

    static class PdfRenderer implements Renderer { public String heading(String t) { return "BT (" + t + ") Tj ET\n"; } }
    static class HtmlRenderer implements Renderer { public String heading(String t) { return "<h1>" + t + "</h1>\n"; } }
    static class MarkdownRenderer implements Renderer { public String heading(String t) { return "# " + t + "\n"; } }

    static class PdfWatermarker implements Watermarker { public String stamp(String c) { return c + "/Watermark (ORIGINAL)\n"; } }
    static class HtmlWatermarker implements Watermarker { public String stamp(String c) { return "<div>ORIGINAL</div>\n" + c; } }
    static class MarkdownWatermarker implements Watermarker { public String stamp(String c) { return "> ORIGINAL\n" + c; } }

    static class PdfExporter implements Exporter { public String export(String c) { return "%PDF-1.7\n" + c + "%%EOF"; } }
    static class HtmlExporter implements Exporter { public String export(String c) { return "<html>" + c + "</html>"; } }
    static class MarkdownExporter implements Exporter { public String export(String c) { return c; } }

    // PROBLEM 1: the client knows every concrete class (new PdfRenderer(), new HtmlExporter()...)
    // PROBLEM 2: big if/else chain to pick the platform
    static String makeInvoice(String platform, String customer) {
        Renderer r; Watermarker w; Exporter e;
        if (platform.equals("pdf")) {
            r = new PdfRenderer(); w = new PdfWatermarker(); e = new PdfExporter();
        } else if (platform.equals("html")) {
            r = new HtmlRenderer(); w = new HtmlWatermarker(); e = new HtmlExporter();
        } else if (platform.equals("markdown")) {
            r = new MarkdownRenderer(); w = new MarkdownWatermarker(); e = new MarkdownExporter();
        } else {
            throw new IllegalArgumentException("Unknown platform " + platform);
        }
        return e.export(w.stamp(r.heading("Invoice for " + customer)));
    }

    // PROBLEM 3: the same if/else block is copy-pasted here, creation logic is duplicated
    static String makeReport(String platform, String title) {
        Renderer r; Watermarker w; Exporter e;
        if (platform.equals("pdf")) {
            r = new PdfRenderer(); w = new PdfWatermarker(); e = new PdfExporter();
        } else if (platform.equals("html")) {
            r = new HtmlRenderer(); w = new HtmlWatermarker(); e = new HtmlExporter();
        } else if (platform.equals("markdown")) {
            r = new MarkdownRenderer(); w = new MarkdownWatermarker(); e = new MarkdownExporter();
        } else {
            throw new IllegalArgumentException("Unknown platform " + platform);
        }
        return e.export(w.stamp(r.heading(title)));
    }

    // PROBLEM 4: nothing stops wrong combinations. This compiles, but the file is broken
    // (pdf text inside html, markdown stamp on it)
    static String mixedByMistake(String customer) {
        Renderer r = new PdfRenderer();
        Watermarker w = new MarkdownWatermarker();
        Exporter e = new HtmlExporter();
        return e.export(w.stamp(r.heading("Invoice for " + customer)));
    }

    // PROBLEM 5: a new family (latex) means editing BOTH methods above, and every other
    // place that has this if/else. Existing code must be changed = open/closed principle is broken.

    public static void main(String[] args) {
        System.out.println(makeInvoice("html", "Acme Ltd"));
        System.out.println(makeReport("pdf", "Q3 Sales"));
        System.out.println("BROKEN MIX:\n" + mixedByMistake("Acme Ltd"));
    }
}
