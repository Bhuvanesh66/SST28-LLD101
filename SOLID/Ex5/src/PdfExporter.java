import java.nio.charset.StandardCharsets;

/**
 * LSP-compliant: Explicitly declares size limit via supports().
 * This ensures callers aren't surprised by the 20-character constraint.
 */
public class PdfExporter implements Exporter {

    @Override
    public boolean supports(ExportRequest req) {
        return req != null && (req.body == null || req.body.length() <= 20);
    }

    @Override
    public ExportResult export(ExportRequest req) {
        if (!supports(req)) {
            // Internal safety check, but the caller should have checked supports()
            throw new IllegalArgumentException("PDF cannot handle content > 20 chars");
        }
        String body = req.body == null ? "" : req.body;
        String fakePdf = "PDF(" + req.title + "):" + body;
        return new ExportResult("application/pdf", fakePdf.getBytes(StandardCharsets.UTF_8));
    }
}
