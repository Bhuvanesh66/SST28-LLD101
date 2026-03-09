package com.example.payments;

public class FastPayAdapter implements PaymentGateway {
    private final FastPayClient client;

    public FastPayAdapter(FastPayClient client) {
        this.client = java.util.Objects.requireNonNull(client, "client");
    }

    @Override
    public String charge(String customerId, int amountCents) {
        java.util.Objects.requireNonNull(customerId, "customerId");
        return client.payNow(customerId, amountCents);
    }
}
