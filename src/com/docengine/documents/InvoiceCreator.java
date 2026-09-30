package com.docengine.documents;

import com.docengine.core.*;

// subject = customer name, detail = amount
public class InvoiceCreator extends DocumentCreator {
    protected Document createDocument(String subject, String detail) { return new Invoice(subject, detail); }
}
