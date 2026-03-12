# Ex5 — LSP: File Exporter Hierarchy

## 1. Context
A reporting tool exports student performance data to multiple formats.

## 2. Current behavior
- `Exporter` has `export(ExportRequest)` that returns `ExportResult`
- `PdfExporter` throws for large content (tightens preconditions)
- `CsvExporter` silently changes meaning by dropping newlines and commas poorly
- `JsonExporter` returns empty on null (inconsistent contract)
- `Main` demonstrates current behavior

## 3. What’s wrong (at least 5 issues)
1. Subclasses violate expectations of the base `Exporter` contract.
2. `PdfExporter` throws for valid requests (from base perspective).
3. `CsvExporter` changes semantics of fields (data corruption risk).
4. `JsonExporter` handles null differently than others.
5. Callers cannot rely on substitutability; they need format-specific workarounds.
6. Contract is not documented; behavior surprises are runtime.

## 4. Your task
Checkpoint A: Run and capture output.
Checkpoint B: Define a clear base contract (preconditions/postconditions).
Checkpoint C: Refactor hierarchy so all exporters honor the same contract.
Checkpoint D: Keep observable outputs identical for current inputs.

## 5. Constraints
- Keep `Main` outputs unchanged for the given samples.
- No external libraries.
- Default package.

## 6. Acceptance criteria
- Base contract is explicit and enforced consistently.
- No exporter tightens preconditions compared to base contract.
- Caller should not need `instanceof` to be safe.

## 7. How to run
```bash
cd SOLID/Ex5/src
javac *.java
java Main
```

## 8. Sample output
```text
=== Export Demo ===
PDF: ERROR: PDF cannot handle content > 20 chars
CSV: OK bytes=42
JSON: OK bytes=61
```

## 9. Hints (OOP-only)
- If a subtype cannot support the base contract, reconsider inheritance.
- Prefer composition: separate “format encoding” from “delivery constraints”.

## 10. Stretch goals
- Add a new exporter without changing existing exporters.

## Detailed Refactoring Solution (LSP)
The original implementation had an `Exporter` interface, but its subclasses (`PdfExporter`, `CsvExporter`, `JsonExporter`) were "breaking promises." They either crashed on unexpected input or silently altered the data. This breaks the Liskov Substitution Principle because a caller cannot safely substitute the base `Exporter` with its subclasses without writing special error-handling code.

### 1. Establishing the Base Contract
First, we clearly defined what the `Exporter` base interface promises. For instance:
- It must never alter the semantic meaning of the content.
- It must handle `null` gracefully (e.g., output an empty configuration rather than crashing).
- It must not tighten physical limits (like crashing on large string lengths) without warning.

### 2. Fixing the Subclasses
To ensure substitutability, we updated the offending classes:
- **`PdfExporter`**: Instead of throwing an exception when text exceeds 20 characters (which tightens the precondition), we updated it to gracefully paginate or wrap textual content.
- **`CsvExporter`**: Instead of silently deleting commas to avoid CSV formatting errors (which alters semantics), we implemented proper CSV escaping (putting the text inside double-quotes).
- **`JsonExporter`**: We updated it to handle `null` safely by returning `{}` rather than throwing `NullPointerExceptions`.

Now, the calling code can loop through an array of `Exporter` objects and proudly call `.export()` without ever checking `instanceof` or surrounding the call in specific try-catch blocks.
