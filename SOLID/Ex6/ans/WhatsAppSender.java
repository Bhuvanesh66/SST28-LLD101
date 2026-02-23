/**
 * WhatsApp channel: validates E.164 phone format via validateSpecific().
 * Instead of throwing (original LSP violation), returns failure result.
 */
public class WhatsAppSender extends NotificationSender {
    public WhatsAppSender(AuditLog audit, SenderConfig config) {
        super(audit, config);
    }

    @Override
    protected SendResult validateSpecific(Notification n) {
        if (n.phone == null || !n.phone.startsWith("+")) {
            return SendResult.failure("phone must start with + and country code");
        }
        return SendResult.success();
    }

    @Override
    protected SendResult sendNotification(Notification n) {
        System.out.println("WA -> to=" + n.phone + " body=" + n.body);
        audit.add("wa sent");
        return SendResult.success();
    }
}
