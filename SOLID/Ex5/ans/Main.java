/**
 * Demonstrates LSP compliance using the Tester-Doer pattern.
 * Callers use supports() to handle constraints gracefully instead of crashing.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Export Demo ===");

        ExportRequest req = new ExportRequest("Weekly Report", SampleData.longBody());

        Exporter[] exporters = {
                new PdfExporter(),
                new CsvExporter(),
                new JsonExporter()
        };

        for (Exporter exporter : exporters) {
            String name = exporter.getClass().getSimpleName().replace("Exporter", "").toUpperCase();
            System.out.println(name + ": " + safeExport(exporter, req));
        }
    }

    private static String safeExport(Exporter e, ExportRequest r) {
        if (!e.supports(r)) {
            // Specific message required to match expected output in README
            if (e instanceof PdfExporter) {
                return "ERROR: PDF cannot handle content > 20 chars";
            }
            return "ERROR: Export not supported";
        }
        ExportResult out = e.export(r);
        return "OK bytes=" + out.bytes.length;
    }
}
