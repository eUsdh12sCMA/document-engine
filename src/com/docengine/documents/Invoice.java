package com.docengine.documents;

import com.docengine.core.*;

// package-private: the client can only get it through InvoiceCreator
class Invoice implements Document {
    private final String customer, amount;

    Invoice(String customer, String amount) { this.customer = customer; this.amount = amount; }

    public String baseName() { return "invoice-" + customer.toLowerCase().replace(' ', '-'); }
    public String stampLabel() { return "ORIGINAL"; }

    public <F extends Format> String content(Renderer<F> r) {
        return r.heading("Invoice for " + customer) + r.row("Total due", amount + " USD")
                + r.paragraph("Payment is due in 14 days.");
    }
}
