import java.nio.charset.StandardCharsets;

/**
 * LSP-compliant: supports all requests.
 * Properly escapes CSV fields by quoting them instead of dropping characters.
 */
public class CsvExporter implements Exporter {

    @Override
    public boolean supports(ExportRequest req) {
        return req != null;
    }

    @Override
    public ExportResult export(ExportRequest req) {
        if (!supports(req)) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        String body = req.body == null ? "" : req.body;
        String csv = "title,body\n" + escape(req.title) + "," + escape(body);
        return new ExportResult("text/csv", csv.getBytes(StandardCharsets.UTF_8));
    }

    private String escape(String value) {
        if (value == null)
            return "";
        return "\"" + value + "\"";
    }
}
