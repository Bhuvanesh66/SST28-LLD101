# Ex2 — SRP: Campus Cafeteria Billing

## 1. Context
A cafeteria billing console generates invoices for student orders. It currently handles menu definition, tax logic, discount logic, invoice formatting, and persistence.

## 2. Current behavior
- Uses an in-memory menu
- Builds an order (hard-coded in `Main`)
- Computes subtotal, tax, discount, and total
- Prints an invoice and writes it to a file-like store (in-memory)

## 3. What’s wrong with the design (at least 5 issues)
1. `CafeteriaSystem.checkout` mixes menu lookup, pricing, tax rules, discount rules, printing, and persistence.
2. Tax rules are hard-coded and not extensible.
3. Discounts are hard-coded with ad-hoc conditions.
4. Invoice formatting is mixed with money calculations.
5. Persistence is a concrete class; hard to test without writing.
6. `Main` depends on too many internal details.

## 4. Your task (refactor plan)
Checkpoint A: Run and capture output.
Checkpoint B: Separate pricing/tax/discount computations into dedicated components.
Checkpoint C: Move invoice formatting out of `CafeteriaSystem`.
Checkpoint D: Introduce small abstractions to decouple persistence and rules.
Checkpoint E: Keep output identical.

## 5. Constraints
- Preserve exact invoice text and line order.
- Keep `MenuItem` and `OrderLine` public fields unchanged.
- No external libraries.

## 6. Acceptance criteria
- `CafeteriaSystem` orchestrates only; it should not format strings or encode tax/discount specifics.
- Adding a new discount should not require editing a long method.

## 7. How to run
```bash
cd SOLID/Ex2/src
javac *.java
java Main
```

## 8. Sample output
```text
=== Cafeteria Billing ===
Invoice# INV-1001
- Veg Thali x2 = 160.00
- Coffee x1 = 30.00
Subtotal: 190.00
Tax(5%): 9.50
Discount: -10.00
TOTAL: 189.50
Saved invoice: INV-1001 (lines=7)
```

## 9. Hints (OOP-only)
- Keep “rules” behind interfaces so new rules can be added without editing a big method.
- Keep formatting as a separate responsibility.

## 10. Stretch goals
- Add a second invoice for a staff member with different discount policy.

## Detailed Refactoring Solution (SRP)
The original `CafeteriaSystem` was a bloated class handling everything from menu lookups to tax calculations and formatting receipts. We broke this down to enforce the Single Responsibility Principle.

### 1. Separating the Math from the System
We isolated the financial logic into dedicated rule components:
- **`TaxCalculator`**: Encapsulates the 5% tax logic. If the tax rate changes, only this class needs updating.
- **`DiscountCalculator`**: Encapsulates the business rules for applying flat or percentage-based discounts.

### 2. Segregating Receipt Formatting
Formatting strings and manipulating currency display is a separate concern from calculating totals. We extracted this into an **`InvoicePrinter`** class. Now, if the cafeteria decides to print receipts in JSON or change the layout, the financial logic isn't touched.

### 3. Abstracting Persistence
We moved the file-saving logic behind an `InvoiceRepository` interface. The main system no longer knows if it's saving to memory, a text file, or a real database.

Ultimately, the `CafeteriaSystem` coordinate the interaction between calculating totals, generating the invoice, and saving it, ensuring each class acts as an isolated, easily testable unit.
