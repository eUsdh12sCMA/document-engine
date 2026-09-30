package com.docengine.html;

import com.docengine.core.Exporter;

class HtmlExporter implements Exporter<Html> {
    public String extension() { return ".html"; }
    public String wrap(String content) { return "<!DOCTYPE html>\n<html><body>\n" + content + "</body></html>"; }
}
