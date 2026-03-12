# Adapter — Payments (Refactoring)

## Narrative (Current Code)
OrderService directly depends on two mismatched SDKs (`FastPayClient`, `SafeCashClient`), uses a string `provider` switch, and duplicates glue logic.

## Your Task
Introduce an **Adapter** so `OrderService` depends only on a `PaymentGateway` interface. Create:
- `PaymentGateway` (target interface): `String charge(String customerId, int amountCents)`
- `FastPayAdapter` and `SafeCashAdapter` mapping to their respective SDKs
- A simple map-based registry in `App` to select the gateway

Refactor `OrderService` to accept a `PaymentGateway` and remove provider branching.

## Acceptance Criteria
- `OrderService` calls **only** `PaymentGateway`
- Adding a new provider requires no change to `OrderService`
- Running `App` prints transaction IDs for both providers

## Hints
- Use constructor injection or a `Map<String, PaymentGateway>`
- Keep adapters stateless
- Use `Objects.requireNonNull` to validate inputs

## Build & Run
```bash
cd adapter-payments/src
javac com/example/payments/*.java
java com.example.payments.App
```

## Detailed Refactoring Solution (Adapter Pattern)
The original `OrderService` was tightly wound around specific third-party SDKs (`FastPayClient` and `SafeCashClient`). Any change to these external libraries, or the addition of a new one, would force us to modify the core `OrderService` logic.

### 1. The Target Interface
We introduced the **Adapter Pattern** by establishing a single "Target" interface that the `OrderService` actually wants to talk to: `PaymentGateway`. This interface specifies a simple `charge(String customerId, int amountCents)` method.

### 2. Creating the Adapters
We then created two Adapter classes:
- **`FastPayAdapter`**: Implements `PaymentGateway`. Inside its `charge()` method, it translates our universal parameters into the specific method calls required by `FastPayClient`.
- **`SafeCashAdapter`**: Implements `PaymentGateway`. Inside its `charge()` method, it translates the parameters for the `SafeCashClient`.

### 3. The Refactored Client
The `OrderService` was updated to accept *only* the `PaymentGateway` interface via Constructor Injection. It no longer contains a massive `switch` statement checking the "provider" string. 

Because of the Adapter, the `OrderService` is entirely insulated from the messy reality of third-party SDKs. New payment gateways can be easily added by writing a new adapter class without ever touching the `OrderService`.
