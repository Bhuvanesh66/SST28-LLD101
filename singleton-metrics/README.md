Exercise A — Singleton Refactoring (Metrics Registry)
----------------------------------------------------
Narrative
A CLI tool called **PulseMeter** collects runtime metrics (counters) and exposes them globally
so any part of the app can increment counters like `REQUESTS_TOTAL`, `DB_ERRORS`, etc.

The current implementation is **not a real singleton**, **not thread-safe**, and is vulnerable to
**reflection** and **serialization** breaking the singleton guarantee.

Your job is to refactor it into a **proper, thread-safe, lazy-initialized Singleton**.

What you have (Starter)
- `MetricsRegistry` is *intended* to be global, but:
  - `getInstance()` can return different objects under concurrency.
  - The constructor is not private.
  - Reflection can create multiple instance s.
  - Serialization/deserialization can produce a new instance.
- `MetricsLoader` incorrectly uses `new MetricsRegistry()`.

Tasks
1) Make `MetricsRegistry` a proper, **thread-safe singleton**
   - **Lazy initialization**
   - **Private constructor**
   - Thread safety: pick one approach (recommended: static holder or double-checked locking)

2) Block reflection-based multiple construction
   - If the constructor is called when an instance already exists, throw an exception
   - (Hint: use a static flag/instance check inside the constructor)

3) Preserve singleton on serialization
   - Implement `readResolve()` so deserialization returns the same singleton instance

4) Update `MetricsLoader` to use the singleton
   - No `new MetricsRegistry()` anywhere in code

Acceptance
- Single instance across threads within a JVM run.
- Reflection cannot construct a second instance.
- Deserialization returns the same instance.
- Loading metrics from `metrics.properties` works.
- Values are accessible via:
  - `increment(key)`
  - `getCount(key)`
  - `getAll()`

Build/Run (Starter)
  cd singleton-metrics/src
  javac com/example/metrics/*.java
  java com.example.metrics.App

Useful Demo Commands (after you fix it)
- Concurrency check:
  java com.example.metrics.ConcurrencyCheck
- Reflection attack check:
  java com.example.metrics.ReflectionAttack
- Serialization check:
  java com.example.metrics.SerializationCheck

Note
This starter is intentionally broken. Some of these checks will "succeed" in breaking the singleton
until you fix the implementation.

## Detailed Refactoring Solution (Singleton Pattern)
The original `MetricsRegistry` claimed to be global, but because it had a public constructor and a flawed `getInstance()` method, multiple threads or pieces of code could accidentally create multiple different registries, causing metrics to be lost.

### 1. The Basic Singleton
To ensure only one instance ever exists, we implemented the true **Singleton Pattern**:
- Made the constructor `private` so nobody else can call `new MetricsRegistry()`.
- Created a `private static` variable to hold the sole instance.
- Provided a `public static getInstance()` method to retrieve it.

### 2. Thread Safety & Lazy Loading
If two threads call `getInstance()` at the exact same millisecond before the instance is created, they might both create one. To fix this safely and efficiently, we used **Double-Checked Locking** (or the Bill Pugh Initialization-on-Demand Holder idiom). This ensures the object is only created exactly when it's first requested (Lazy Loading), but securely blocks additional threads from creating duplicates.

### 3. Stopping Attacks
Java has advanced features that can break simple Singletons:
- **Reflection**: A hacker could use Java Reflection to forcefully change the constructor from `private` to `public`. To stop this, we added code *inside* the constructor that throws an Exception if `instance != null`.
- **Serialization**: If the Singleton is saved to a file and loaded back into memory, Java creates a new object. We stopped this by implementing the `readResolve()` method, instructing Java to discard the newly loaded object and just return the existing `getInstance()` instead.
