# Assignment 2 Report -
# Document Generation Engine

## 1. Domain Selection
For this project I built a Document Generation Engine. It creates business documents and output them in different formats. 
* **Product Types:** `Renderer` (creates the text structure), `Watermarker` (adds labels like "ORIGINAL"), and `Exporter` (wraps it into a file)
* **Product Families:** `Pdf`, `Html`, `Markdown`, and `Latex`.

## Part A: Start Without Factories
I started by writing `LegacyClient.java` to show the problems of not using patterns:
1. **Duplicate logic:** The big `if/else` block that checks the format is copied inside every method
2. **Hard to extend:** If I want to add a new format I have to open this class and edit every `if/else` block which breaks the Open/Closed principle
3. **Accidental mixing:** The method `mixedByMistake()` shows that I can easily create a PDF Renderer and an HTML Exporter. The Java compiler does not stop this, but the generated file is broken

## Part B: Factory Method
I used the Factory Method to hide how documents are created.
* **Product:** `Document` interface (with concrete classes `Invoice`, `Report`, `Contract`)
* **Creator:** `DocumentCreator` abstract class
This isn't a simple static factory because the `DocumentCreator` has business logic inside its `generate()` method. It checks the input and calls `createDocument()`, applies the watermark, and exports the file

## Part C: Abstract Factory
To fix the format problem there is `DocumentFactory` interface. Every factory (like `PdfFactory` or `HtmlFactory`) only returns the `Renderer`, `Watermarker`, and `Exporter` that belong to its own family

## Part D: Compatibility Rule
I made the design itself block it using Java Generics. 
I created a `Format` interface and marker classes like `Pdf` and `Html`. The factory uses `<F extends Format>`. If someone writes `Renderer<Pdf> r = new HtmlFactory().createRenderer();`, the Java compiler gives an "incompatible types" error. I proved this works in my `CompatibilityTest.java` by running the real Java compiler from the test

## Part E: Runtime Factory Selection
The client code never calls `new PdfFactory()` directly so instead there are two classes for this
* `FamilySelector`: It looks for `family= ` in the command line first. If absent, it checks the `DOC_FAMILY` environment variable, and defaults to `"pdf"` if nothing is found
* `FactoryRegistry`: It takes the string name and returns the correct factory object using a map

## Part F: Business Scenarios
My `DocumentService` class has real business operations where the products collab:
* `issueInvoice()`: Generates an invoice and stamps it as "ORIGINAL"
* `signContract()`: Generates a contract and stamps it as "LEGAL COPY"
* `closeMonth()`: Combines an invoice and a report into a list and making sure both use the same format family.
* `previewContract()`: Creates a fast draft which is not exported to a file

## Part G: Add a New Family
I only had to create the `Latex` package and add one single line in `FactoryRegistry` to map the string "latex" to `LatexFactory::new`. I didn't change any code in `DocumentService`, `DocumentCreator`, or the original document classes, and the existing business logic was not modified

## Part H: UML
The UML diagram is included in the `doc/` directory. It shows all the relationships and marks the Factory Method and Abstract Factory parts.

## Part I: Automated Tests
I wrote full tests using JUnit 5. The `.jar` file for testing is placed in the `lib/` folder so it can be executed easily from the command line without Maven.
* Creation of all families and products
* A strict compiler test for compatibility between products
* Runtime selection logic
* Business behavior (checking if HTML has tags and Markdown has correct syntax)
* Negative tests (trying to pass empty strings)

## Part J: Git History

1. Initial domain model without factories (`LegacyClient`)
2. Add Factory Method (`DocumentCreator` and `Document`)
3. Add product abstractions (`Renderer`, `Watermarker`, `Exporter`)
4. Add Abstract Factory for Pdf, Html, Markdown
5. Implement Generics for compile-time compatibility rule
6. Add runtime factory selection logic
7. Add JUnit tests and compiler checks
8. Add Latex product family
9. Add docs, UML, REPORT and README