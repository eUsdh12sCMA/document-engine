package com.docengine.documents;

import com.docengine.core.*;

// subject = report title, detail = summary text
public class ReportCreator extends DocumentCreator {
    protected Document createDocument(String subject, String detail) { return new Report(subject, detail); }
}
