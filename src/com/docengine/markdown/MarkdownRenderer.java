package com.docengine.markdown;

import com.docengine.core.Renderer;

class MarkdownRenderer implements Renderer<Markdown> {
    public String heading(String text) { return "# " + text + "\n\n"; }
    public String paragraph(String text) { return text + "\n\n"; }
    public String row(String label, String value) { return "**" + label + "**: " + value + "\n\n"; }
}
