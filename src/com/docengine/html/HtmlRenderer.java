package com.docengine.html;

import com.docengine.core.Renderer;

class HtmlRenderer implements Renderer<Html> {
    public String heading(String text) { return "<h1>" + esc(text) + "</h1>\n"; }
    public String paragraph(String text) { return "<p>" + esc(text) + "</p>\n"; }
    public String row(String label, String value) { return "<p><b>" + esc(label) + "</b>: " + esc(value) + "</p>\n"; }

    // html needs escaping, other formats do not
    private String esc(String text) { return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"); }
}
