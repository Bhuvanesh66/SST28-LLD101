package com.example.payments;

public class SafeCashAdapter implements PaymentGateway {
    private final SafeCashClient client;

    public SafeCashAdapter(SafeCashClient client) {
        this.client = java.util.Objects.requireNonNull(client, "client");
    }

    @Override
    public String charge(String customerId, int amountCents) {
        java.util.Objects.requireNonNull(customerId, "customerId");
        return client.createPayment(amountCents, customerId).confirm();
    }
}
