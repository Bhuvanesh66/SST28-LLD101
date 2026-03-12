Flyweight — Deduplicate Map Marker Styles (Refactoring)
------------------------------------------------------
Narrative (Current Code)
A CLI tool called **GeoDash** renders a large list of map markers (pins).
Right now, every `MapMarker` stores its own style fields (shape, color, size, filled).
When rendering thousands of markers, we end up creating thousands of duplicate style objects → memory blow-up.

Your Task
1) Extract an immutable `MarkerStyle` (shape, color, size, filled) as **intrinsic state**.
2) Implement `MarkerStyleFactory` that caches and returns shared `MarkerStyle` instances by key.
3) Modify `MapMarker` to hold:
   - `MarkerStyle` (intrinsic)
   - marker-specific fields (extrinsic): `lat`, `lng`, `label`
4) Update `MapDataSource` (marker creation pipeline) to obtain styles via the factory
   (no `new MarkerStyle(...)` during marker creation).

Acceptance Criteria
- Same rendering “cost” as before (same number of markers rendered, same output format).
- Identical style configurations reuse the same `MarkerStyle` instance
  (see `QuickCheck` — it should report a small number of unique styles).
- `MarkerStyle` is immutable (all fields final, no setters).
- `MapMarker` stores only extrinsic state plus a reference to shared `MarkerStyle`.

Hints
- Use a `Map<String, MarkerStyle>` cache in the factory.
- Key suggestion: `"PIN|RED|12|F"` (shape|color|size|filledFlag)

Build & Run
  cd flyweight-markers/src
  javac com/example/map/*.java
  java com.example.map.App

Repo intent
This is a **refactoring assignment**: the starter code is intentionally wasteful.
Students should refactor to Flyweight without changing the external behavior.

## Detailed Refactoring Solution (Flyweight Pattern)
The original `MapMarker` class was highly inefficient. If you rendered 10,000 red pins, you were creating 10,000 separate `String color = "RED"` and `String shape = "PIN"` objects in memory. The **Flyweight Pattern** solves this by sharing identical data.

### 1. Identifying Intrinsic vs. Extrinsic State
- **Intrinsic State** (Data that is the same for many objects): `shape`, `color`, `size`, `filled`. We extracted this into an immutable class called **`MarkerStyle`**.
- **Extrinsic State** (Data that is strictly unique per object): `lat`, `lng`, `label`. This remained inside **`MapMarker`**.

### 2. The Flyweight Factory
We created the **`MarkerStyleFactory`** to act as a caching mechanism. It holds a `HashMap<String, MarkerStyle>`.
When the system needs a red, filled pin of size 12, it asks the factory.
- If that specific `MarkerStyle` has been requested before, the factory returns the exact same object from its cache.
- If it hasn't, it creates it, stores it in the cache, and then returns it.

### 3. The Refactored MapMarker
The `MapMarker` was updated to store its unique coordinates (`lat`/`lng`), and a *reference* to the shared `MarkerStyle` object. 

As a result, rendering 10,000 identical red pins now only requires exactly **1** `MarkerStyle` object in memory, rather than 10,000, saving a massive amount of RAM without changing how the map looks.
