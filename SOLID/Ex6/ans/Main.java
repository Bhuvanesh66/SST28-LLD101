/**
 * Main demonstrates LSP via Template Method:
 * - All senders used through base NotificationSender type.
 * - NO try/catch needed.
 * - send() is final in base — subtypes cannot break the contract.
 * - SenderConfig controls channel limits (email=unlimited, SMS=160,
 * WA=unlimited).
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Notification Demo ===");
        AuditLog audit = new AuditLog();

        Notification n = new Notification("Welcome", "Hello and welcome to SST!", "riya@sst.edu", "9876543210");

        SenderConfig emailConfig = new SenderConfig(10000); // email: practically unlimited
        SenderConfig smsConfig = new SenderConfig(160); // SMS: 160 char limit
        SenderConfig waConfig = new SenderConfig(10000); // WA: practically unlimited

        NotificationSender email = new EmailSender(audit, emailConfig);
        NotificationSender sms = new SmsSender(audit, smsConfig);
        NotificationSender wa = new WhatsAppSender(audit, waConfig);

        email.send(n);
        sms.send(n);

        SendResult waResult = wa.send(n);
        if (!waResult.isSuccess()) {
            System.out.println("WA ERROR: " + waResult.getError());
            audit.add("WA failed");
        }

        System.out.println("AUDIT entries=" + audit.size());
    }
}
