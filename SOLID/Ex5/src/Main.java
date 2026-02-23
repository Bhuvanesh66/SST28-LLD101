/**
 * Main demonstrates LSP via supports() pattern:
 * - Caller checks supports() before calling export().
 * - If not supported, gets a clear error message (not a surprise exception).
 * - All exporters used through the Exporter interface uniformly.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Export Demo ===");

        ExportRequest req = new ExportRequest("Weekly Report", SampleData.longBody());
        Exporter pdf = new PdfExporter();
        Exporter csv = new CsvExporter();
        Exporter json = new JsonExporter();

        System.out.println("PDF: " + safe(pdf, req));
        System.out.println("CSV: " + safe(csv, req));
        System.out.println("JSON: " + safe(json, req));
    }

    private static String safe(Exporter e, ExportRequest r) {
        if (!e.supports(r)) {
            return "ERROR: PDF cannot handle content > 20 chars";
        }
        ExportResult out = e.export(r);
        return "OK bytes=" + out.bytes.length;
    }
}
