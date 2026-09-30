# Document Generation Engine

This is my solution for Assignment 2. The system generates different business documents and formats them for various platforms using Factory Method and Abstract Factory patterns.

## Architecture Overview
The engine creates documents based on product families and types:
* **4 Product Families:** `Pdf`, `Html`, `Markdown`, and `Latex`.
* **3 Abstract Factory Products:** `Renderer` (for text), `Watermarker` (for stamps), and `Exporter` (for saving the file).
* **Factory Method Products:** `Invoice`, `Report`, and `Contract`.

## How to Build and Run

To compile and run the project, use these commands:

```bash
find src -name "*.java" > sources.txt
mkdir -p out && javac --release 17 -d out @sources.txt
java -cp out com.docengine.app.Main family=html      # or family=pdf|markdown|latex
DOC_FAMILY=markdown java -cp out com.docengine.app.Main
java -cp out com.docengine.legacy.LegacyClient       # Part A (no factories)