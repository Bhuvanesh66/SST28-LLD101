/**
 * Channel-specific configuration.
 * Used by base class to enforce common limits (e.g., maxLen).
 */
public class SenderConfig {
    public int maxLen = 160;

    public SenderConfig() {
    }

    public SenderConfig(int maxLen) {
        this.maxLen = maxLen;
    }
}
