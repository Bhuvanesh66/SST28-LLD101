/**
 * LSP-compliant contract:
 * - supports() allows callers to check capability without surprise exceptions.
 * - export() carries out the export only if supported.
 * - This creates a "Tester-Doer" pattern which prevents precondition
 * strengthening issues.
 */
public interface Exporter {
    boolean supports(ExportRequest req);

    ExportResult export(ExportRequest req);
}
