/**
 * Email channel: validates email address, sends full body without truncation.
 */
public class EmailSender extends NotificationSender {
    public EmailSender(AuditLog audit, SenderConfig config) {
        super(audit, config);
    }

    @Override
    protected SendResult validateSpecific(Notification n) {
        if (n.email == null || !n.email.contains("@")) {
            return SendResult.failure("EMAIL ERROR: invalid email address");
        }
        return SendResult.success();
    }

    @Override
    protected SendResult sendNotification(Notification n) {
        System.out.println("EMAIL -> to=" + n.email + " subject=" + n.subject + " body=" + n.body);
        audit.add("email sent");
        return SendResult.success();
    }
}
