# Ex9 — DIP: Assignment Evaluation Pipeline

## 1. Context
An evaluation pipeline checks submissions using a rubric, runs plagiarism checks, grades code, and writes a report.

## 2. Current behavior
- `EvaluationPipeline.evaluate` directly instantiates concrete graders/checkers/writers with `new`
- Prints a final summary line and “writes” a report

## 3. What’s wrong (at least 5 issues)
1. High-level pipeline depends on low-level concrete classes (hard-coded `new` everywhere).
2. Hard to test pipeline without running real checks.
3. Changing a component requires editing pipeline code.
4. No clear abstraction boundaries; responsibilities are mixed.
5. Configuration is embedded (paths, thresholds).

## 4. Your task
Checkpoint A: Run and capture output.
Checkpoint B: Introduce small abstractions for grader/checker/writer.
Checkpoint C: Inject dependencies into pipeline.
Checkpoint D: Keep output identical.

## 5. Constraints
- Preserve output and line order.
- Keep `Submission` fields unchanged.
- No external libraries.

## 6. Acceptance criteria
- Pipeline depends on abstractions, not concretes.
- Easy to substitute test doubles.

## 7. How to run
```bash
cd SOLID/Ex9/src
javac *.java
java Main
```

## 8. Sample output
```text
=== Evaluation Pipeline ===
PlagiarismScore=12
CodeScore=78
Report written: report-23BCS1007.txt
FINAL: PASS (total=90)
```

## 9. Hints (OOP-only)
- Define minimal interfaces with only what the pipeline needs.
- Pass dependencies via constructor.

## 10. Stretch goals
- Add a second grading strategy without editing pipeline logic.

## Detailed Refactoring Solution (DIP)
The `EvaluationPipeline` violated the Dependency Inversion Principle because a high-level module (the pipeline orchestrator) instantiated low-level concrete modules (the specific graders and checkers) directly using the `new` keyword.

### 1. Introducing Abstractions
We created simple, focused interfaces for every step of the pipeline:
- **`PlagiarismChecker`** interface (abstracts the check logic)
- **`Grader`** interface (abstracts the grading logic)
- **`ReportWriter`** interface (abstracts the file generation)

The concrete classes (`BasicPlagiarismChecker`, `StandardGrader`, `FileReportWriter`) were updated to simply `implement` these interfaces.

### 2. Constructor Injection
We modified the `EvaluationPipeline` to remove all the `new` keywords. Instead, we injected the dependencies through its constructor:
```java
public EvaluationPipeline(PlagiarismChecker checker, Grader grader, ReportWriter writer) {
    this.checker = checker;
    this.grader = grader;
    this.writer = writer;
}
```

**Why this works:** The high-level pipeline now depends strictly on *abstractions*, not concretes. This allows us to easily pass in a "Mock" `ReportWriter` during testing, or swap out the `BasicPlagiarismChecker` for an `AdvancedAIPlagiarismChecker` later without ever editing the `EvaluationPipeline` class.
