Exercise B — Immutable Classes (Incident Tickets)
------------------------------------------------
Narrative
A small CLI tool called **HelpLite** creates and manages support/incident tickets.
Today, `IncidentTicket` is **mutable**:
- multiple constructors
- public setters
- validation scattered across the codebase
- objects can be modified after being "created", causing audit/log inconsistencies

Refactor the design so `IncidentTicket` becomes **immutable** and is created using a **Builder**.

What you have (Starter)
- `IncidentTicket` has public setters + several constructors.
- `TicketService` creates a ticket, then mutates it later (bad).
- Validation is duplicated and scattered, making it easy to miss checks.
- `TryIt` demonstrates how the same object can change unexpectedly.

Tasks
1) Refactor `IncidentTicket` to an **immutable class**
   - private final fie lds
   - no setters
   - defensive copying for collections
   - safe getters (no internal state leakage)

2) Introduce `IncidentTicket.Builder`
   - Required: `id`, `reporterEmail`, `title`
   - Optional: `description`, `priority`, `tags`, `assigneeEmail`, `customerVisible`, `slaMinutes`, `source`
   - Builder should be fluent (`builder().id(...).title(...).build()`)

3) Centralize validation
   - Move ALL validation to `Builder.build()`
   - Use helpers in `Validation.java` (add more if needed)
   - Examples:
     - id: non-empty, length <= 20, only [A-Z0-9-] (you can reuse helper)
     - reporterEmail/assigneeEmail: must look like an email
     - title: non-empty, length <= 80
     - priority: one of LOW/MEDIUM/HIGH/CRITICAL
     - slaMinutes: if provided, must be between 5 and 7,200

4) Update `TicketService`
   - Stop mutating a ticket after creation
   - Any “updates” should create a **new** ticket instance (e.g., by Builder copy/from method)
   - Keep the API simple; you can add `toBuilder()` or `Builder.from(existing)`

Acceptance
- `IncidentTicket` has no public setters and fields are final.
- Tickets cannot be modified after creation (including tags list).
- Validation happens only in one place (`build()`).
- `TryIt` still works, but now demonstrates immutability (attempted mutations should not compile or have no effect).
- Code compiles and runs with the starter commands below.

Build/Run (Starter demo)
  cd immutable-tickets/src
  javac com/example/tickets/*.java TryIt.java
  java TryIt

Tip
After refactor, you can update `TryIt` to show:
- building a ticket
- tags list is not mutable from outside

## Detailed Refactoring Solution (Immutable & Builder Patterns)
The original `IncidentTicket` class was highly dangerous. Because it was mutable (having public setters), any piece of code could accidentally change a ticket's status or assignee long after creation, leading to auditing nightmares.

### 1. Achieving Immutability
We locked down the `IncidentTicket` class:
- All fields were made `private final`.
- All `setX()` methods were deleted.
- The constructor was made private.
- Collections (like `tags`) were defensively copied so external lists couldn't mutate the internal state.

### 2. Implementing the Builder
To handle the difficult process of constructing an object with many optional fields, we introduced the **Builder Pattern** (`IncidentTicket.Builder`).
The builder uses fluent methods (`builder.title("...").priority("...")`) to gather the data.

### 3. Centralizing Validation
Previously, validation rules were scattered across the codebase. We moved **all** rules (like ensuring `id` formatting or checking `slaMinutes`) directly into the `Builder.build()` method. Now, it is literally impossible to construct an invalid `IncidentTicket`.

### 4. Updating the System
Instead of calling `ticket.setAssignee(...)` to update a record, the system now uses a "copy constructor" approach (e.g., `ticket.toBuilder().assignee(...).build()`). This creates a brand new, valid, immutable ticket object, perfectly preserving the audit trail.