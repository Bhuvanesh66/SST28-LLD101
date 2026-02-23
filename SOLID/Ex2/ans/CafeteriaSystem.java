import java.util.*;

public class CafeteriaSystem {
    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private final TaxCalculator taxCalc;
    private final DiscountCalculator discountCalc;
    private final InvoiceStore store;
    private final InvoicePrinter printer;
    private int invoiceSeq = 1000;

    public CafeteriaSystem(TaxCalculator taxCalc, DiscountCalculator discountCalc,
            InvoiceStore store, InvoicePrinter printer) {
        this.taxCalc = taxCalc;
        this.discountCalc = discountCalc;
        this.store = store;
        this.printer = printer;
    }

    public void addToMenu(MenuItem i) {
        menu.put(i.id, i);
    }

    public void checkout(String customerType, List<OrderLine> lines) {
        String invId = "INV-" + (++invoiceSeq);

        double subtotal = 0.0;
        for (OrderLine l : lines) {
            MenuItem item = menu.get(l.itemId);
            subtotal += item.price * l.qty;
        }

        double taxPct = taxCalc.taxPercent(customerType);
        double tax = subtotal * (taxPct / 100.0);

        double discount = discountCalc.discountAmount(customerType, subtotal, lines.size());

        double total = subtotal + tax - discount;

        String printable = printer.format(invId, lines, menu, subtotal, taxPct, tax, discount, total);
        System.out.print(printable);

        store.save(invId, printable);
        System.out.println("Saved invoice: " + invId + " (lines=" + store.countLines(invId) + ")");
    }
}
