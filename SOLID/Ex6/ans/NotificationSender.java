/**
 * LSP-compliant base class using Template Method pattern.
 *
 * send() is FINAL — subtypes cannot break the contract.
 * They can only fill in:
 * - validateSpecific() for channel-specific validation
 * - sendNotification() for actual sending
 *
 * Common validation (body not empty, maxLen) is enforced in the base.
 * SenderConfig provides channel-specific limits.
 */
public abstract class NotificationSender {
    protected final AuditLog audit;
    protected final SenderConfig config;

    protected NotificationSender(AuditLog audit, SenderConfig config) {
        this.audit = audit;
        this.config = config;
    }

    // Template method — controls the flow. Subtypes CANNOT override this.
    public final SendResult send(Notification n) {
        // Step 1: Common validation
        if (n.body == null || n.body.isEmpty()) {
            return SendResult.failure("Body is empty");
        }
        if (n.body.length() > config.maxLen) {
            return SendResult.failure("Body too long");
        }

        // Step 2: Channel-specific validation (subtypes override this)
        SendResult validation = validateSpecific(n);
        if (!validation.isSuccess()) {
            return validation;
        }

        // Step 3: Actual sending (subtypes override this)
        return sendNotification(n);
    }

    // Hook for channel-specific validation. Default: no extra checks.
    protected SendResult validateSpecific(Notification n) {
        return SendResult.success();
    }

    // Subtypes implement this to do the actual sending.
    protected abstract SendResult sendNotification(Notification n);
}
