/**
 * LSP-compliant contract:
 * - supports() checks if exporter can handle the request (no surprises).
 * - export() carries out the export only if supported.
 * - Callers check supports() first to avoid exceptions.
 */
public interface Exporter {
    boolean supports(ExportRequest req);

    ExportResult export(ExportRequest req);
}
