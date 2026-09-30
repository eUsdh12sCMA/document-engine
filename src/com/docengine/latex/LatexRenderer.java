package com.docengine.latex;

import com.docengine.core.Renderer;

class LatexRenderer implements Renderer<Latex> {
    public String heading(String text) { return "\\section{" + text + "}\n"; }
    public String paragraph(String text) { return text + "\\par\n"; }
    public String row(String label, String value) { return "\\textbf{" + label + "}: " + value + "\\par\n"; }
}
