# Ex7 — ISP: Smart Classroom Devices Interface

## 1. Context
A smart classroom controller manages devices: projector, lights, AC, attendance scanner.

## 2. Current behavior
- There is one large interface `SmartClassroomDevice` containing many methods
- Each device implements methods it does not need using dummy logic
- Controller calls only some methods depending on device type

## 3. What’s wrong (at least 5 issues)
1. Fat interface forces irrelevant methods on devices.
2. Dummy implementations hide bugs and create misleading behavior.
3. Controller is tempted to call methods that some devices don’t meaningfully support.
4. Adding a new device forces implementing many unrelated methods.
5. Device capabilities are unclear; interface does not model reality.

## 4. Your task
- Split the fat interface into smaller capability-based interfaces.
- Update controller and devices to depend only on what they use.
- Preserve console output.

## 5. Constraints
- Preserve output for `Main`.
- Keep device class names unchanged.
- No external libs.

## 6. Acceptance criteria
- No device implements methods irrelevant to it.
- Controller depends only on specific capability interfaces.

## 7. How to run
```bash
cd SOLID/Ex7/src
javac *.java
java Main
```

## 8. Sample output
```text
=== Smart Classroom ===
Projector ON (HDMI-1)
Lights set to 60%
AC set to 24C
Attendance scanned: present=3
Shutdown sequence:
Projector OFF
Lights OFF
AC OFF
```

## 9. Hints (OOP-only)
- Capabilities: power control, brightness control, temperature control, scanning.
- Keep composition: registry can return devices by capability rather than by concrete class.

## 10. Stretch goals
- Add a “smart board” device without implementing unrelated methods.

## Detailed Refactoring Solution (ISP)
The original `SmartClassroomDevice` was a "fat" interface containing methods that not every device could perform (e.g., `setTemperature` on a Light, or `scanStudent` on an AC). This violates the Interface Segregation Principle because classes (devices) were forced to implement methods they didn't need, usually by returning dummy values or throwing exceptions.

### 1. Segregating by Capability
We resolved this by breaking the giant interface into several tiny, highly-specific interfaces based on actual device capabilities:
- **`PowerControl`**: For turning devices ON/OFF.
- **`TemperatureControl`**: Specific to the Air Conditioner or smart heaters.
- **`BrightnessControl`**: Specific to the Lights or Projector.
- **`AttendanceScanner`**: Specific to the RFID scanner.

### 2. Selective Implementation
Now, a `LightsPanel` class only implements `PowerControl` and `BrightnessControl`. It completely ignores `TemperatureControl`. It is no longer forced to provide a fake implementation for unrelated methods.

### 3. Smart Controller
When the `SmartController` needs to adjust the temperature, it doesn't query a generic "device". It specifically looks for objects that implement the `TemperatureControl` interface. This prevents runtime errors and guarantees that the device actually supports the action being requested.
