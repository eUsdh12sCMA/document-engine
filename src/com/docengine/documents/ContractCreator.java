package com.docengine.documents;

import com.docengine.core.*;

// subject = our company, detail = partner company
public class ContractCreator extends DocumentCreator {
    protected Document createDocument(String subject, String detail) { return new Contract(subject, detail); }
}
