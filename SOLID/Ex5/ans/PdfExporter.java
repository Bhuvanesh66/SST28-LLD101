import java.nio.charset.StandardCharsets;

/**
 * LSP-compliant: supports() declares the size limit upfront.
 * Caller can check supports() before calling export().
 * No surprise exception when used through the Exporter interface.
 */
public class PdfExporter implements Exporter {

    @Override
    public boolean supports(ExportRequest req) {
        return req != null && (req.body == null || req.body.length() <= 20);
    }

    @Override
    public ExportResult export(ExportRequest req) {
        if (!supports(req)) {
            throw new IllegalArgumentException("PDF cannot handle content > 20 chars");
        }
        String body = req.body == null ? "" : req.body;
        String fakePdf = "PDF(" + req.title + "):" + body;
        return new ExportResult("application/pdf", fakePdf.getBytes(StandardCharsets.UTF_8));
    }
}
