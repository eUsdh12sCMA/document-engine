package com.docengine.documents;

import com.docengine.core.*;

class Contract implements Document {
    private final String company, partner;

    Contract(String company, String partner) { this.company = company; this.partner = partner; }

    public String baseName() { return "contract-" + partner.toLowerCase().replace(' ', '-'); }
    public String stampLabel() { return "LEGAL COPY"; }

    public <F extends Format> String content(Renderer<F> r) {
        return r.heading("Service Contract") + r.paragraph(company + " provides services to " + partner + ".")
                + r.row("Signed by", company + " / " + partner);
    }
}
