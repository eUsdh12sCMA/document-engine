package com.docengine.documents;

import com.docengine.core.*;

class Report implements Document {
    private final String title, summary;

    Report(String title, String summary) { this.title = title; this.summary = summary; }

    public String baseName() { return "report-" + title.toLowerCase().replace(' ', '-'); }
    public String stampLabel() { return "CONFIDENTIAL"; }

    public <F extends Format> String content(Renderer<F> r) {
        return r.heading(title) + r.paragraph(summary) + r.row("Status", "Final");
    }
}
