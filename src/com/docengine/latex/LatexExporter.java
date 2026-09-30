package com.docengine.latex;

import com.docengine.core.Exporter;

class LatexExporter implements Exporter<Latex> {
    public String extension() { return ".tex"; }
    public String wrap(String content) {
        return "\\documentclass{article}\n\\begin{document}\n" + content + "\\end{document}";
    }
}
