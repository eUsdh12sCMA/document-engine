package com.docengine.core;

// FACTORY METHOD creator.
// This is not a static factory: generate() has real logic and it calls createDocument(),
// which is abstract, so every subclass decides the product. The client never calls it directly.
public abstract class DocumentCreator {

    // the factory method
    protected abstract Document createDocument(String subject, String detail);

    // business logic is the same for all documents, only the created Document changes
    public final <F extends Format> ExportedFile generate(DocumentFactory<F> factory, String subject, String detail) {
        Document doc = createDocument(check(subject), check(detail));
        String body = doc.content(factory.createRenderer());
        String stamped = factory.createWatermarker().stamp(body, doc.stampLabel());
        return factory.createExporter().export(doc.baseName(), stamped);
    }

    // draft preview: no export, always stamped as DRAFT
    public final <F extends Format> String preview(DocumentFactory<F> factory, String subject, String detail) {
        Document doc = createDocument(check(subject), check(detail));
        return factory.createWatermarker().stamp(doc.content(factory.createRenderer()), "DRAFT");
    }

    // we do not want empty documents
    private String check(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Document data must not be empty");
        return value;
    }
}
