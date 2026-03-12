Proxy — Secure & Lazy-Load Reports (Refactoring)
------------------------------------------------
Narrative (Current Code)
A small CLI tool called CampusVault opens internal reports for different users.
Right now, ReportViewer talks directly to ReportFile and eagerly loads the report content every time.

Problems in the current design:
- No access control: any user can open any report.
- No lazy loading: expensive file loading happens immediately on each open.
- No caching: the same report may be loaded multiple times unnecessarily.
- Clients depend directly on the concrete implementation.

Your Task
1) Introduce a Report abstraction.
2) Keep the expensive file-reading logic inside a real subject (for example, RealReport).
3) Add a ReportProxy that:
   - checks whether the user is allowed to access the report
   - lazy-loads the real report only when needed
   - reuses the loaded real report for repeated views through the same proxy
4) Update ReportViewer / App so clients use the proxy instead of directly using the concrete file loader.

Acceptance Criteria
- Unauthorized users cannot view restricted reports.
- Real report loading happens only when access is granted.
- Real report content is loaded lazily (not during proxy construction).
- Repeated views of the same report through the same proxy should not reload the file every time.
- Output remains easy to verify from console logs.

Hints
- Define an interface: Report { void display(User user); }
- Let RealReport do the expensive load.
- Let ReportProxy hold metadata + a nullable RealReport reference.
- Add logs so it is obvious whether a report was really loaded.

Build & Run
  cd proxy-reports/src
  javac com/example/reports/*.java
  java com.example.reports.App

Repo intent
This is a refactoring assignment: the starter code works, but it does not use Proxy properly.
Students should refactor the design so access control + lazy loading happen via a proxy.

## Detailed Refactoring Solution (Proxy Pattern)
The original `ReportFile` class was inefficient and insecure. The moment it was instantiated, it immediately performed an expensive disk read, and there were no checks to see if the user was actually allowed to look at the file.

### 1. The Proxy Interface
We applied the **Proxy Pattern** by creating a common `Report` interface with a `display(User)` method. Both the real implementation and our new proxy will implement this.

### 2. The Real Subject
We refactored the original reading logic into `RealReport`. However, we updated it so that when `RealReport` is instantiated, it reads the disk *once* and saves the content to a local variable (Caching).

### 3. The Proxy Subject
We created `ReportProxy`. This class acts as a bouncer and an efficiency manager for `RealReport`.
When `ReportProxy` is created, it does **not** load the file. It only stores the file ID.
When `ReportProxy.display(User)` is called:
1.  **Access Control**: It checks if the User has the right permissions. If not, it rejects them immediately.
2.  **Lazy Loading**: If the user is allowed, it checks if the `RealReport` object exists. If it doesn't, it instantiates `RealReport` (which reads the file).
3.  **Delegation**: It then delegates the display command to the `RealReport`.

Because the client interacts with the `Report` interface, it has no idea it is talking to a security proxy instead of the real file loader.
