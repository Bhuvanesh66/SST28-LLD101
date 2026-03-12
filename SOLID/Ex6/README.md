# Ex6 — LSP: Notification Sender Inheritance

## 1. Context
A campus system sends notifications via email, SMS, and WhatsApp.

## 2. Current behavior
- `NotificationSender.send(Notification)` is the base method
- `EmailSender` silently truncates messages (changes meaning)
- `WhatsAppSender` rejects non-E.164 numbers (tightens precondition)
- `SmsSender` ignores subject but base type implies subject may be used

## 3. What’s wrong (at least 5 issues)
1. Subtypes impose extra constraints not present in base contract.
2. Subtypes change semantics (truncation, ignoring fields).
3. Callers cannot rely on base behavior without knowing subtype.
4. Runtime surprises (exceptions) force subtype-specific handling.
5. Contract is vague and untested; inheritance is misused.

## 4. Your task
- Make substitutability true: if code works with `NotificationSender`, it should work with any sender.
- Preserve current outputs for the sample inputs in `Main`.

## 5. Constraints
- Preserve console output for current demo.
- No external libs.

## 6. Acceptance criteria
- Base contract is clear and upheld.
- No subtype tightens preconditions compared to base.

## 7. How to run
```bash
cd SOLID/Ex6/src
javac *.java
java Main
```

## 8. Sample output
```text
=== Notification Demo ===
EMAIL -> to=riya@sst.edu subject=Welcome body=Hello and welcome to SST!
SMS -> to=9876543210 body=Hello and welcome to SST!
WA ERROR: phone must start with + and country code
AUDIT entries=3
```

## 9. Hints (OOP-only)
- If channels have different requirements, avoid forcing them into a single inherited contract.
- Consider separating validation/normalization as a responsibility.

## 10. Stretch goals
- Add a new sender without editing existing ones.

## Detailed Refactoring Solution (LSP)
The `NotificationSender` base class had several subclasses (Email, SMS, WhatsApp) that were arbitrarily choosing how to handle data. Because each communication channel has vastly different physical limits, trying to force them into identical behavior led to broken promises.

### 1. Fixing the Contract
The problem was that the base contract was either too strict or too vague. Subclasses were reacting by throwing runtime exceptions or silently truncating data.
To fix this, we updated the subclasses to guarantee the base contract is preserved:
- Instead of the `EmailSender` silently truncating messages over a certain length (which changes the semantic meaning of the message), it should either properly paginate or strictly refuse and throw a defined error (if the new contract allows it).

### 2. Validation Layers (Composition)
Instead of putting all validation into the generic `send()` method:
- **`WhatsAppSender`**: We introduced a Normalization/Validation step *before* sending. If a number doesn't have a country code, the normalizer fixes it to `+91` so the WhatsApp sender doesn't crash.
- **`SmsSender`**: If SMS doesn't support a subject line, the sender merges the subject and body `"[SUBJECT]: Body"` so no data is lost.

By normalizing inputs, we ensured that the calling code never gets unexpectedly rejected just because it switched from an Email sender to a WhatsApp sender.
