public class ClassroomController {
    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) {
        this.reg = reg;
    }

    public void startClass() {
        InputControl pj = reg.getFirstOfType(InputControl.class);
        pj.powerOn();
        pj.connectInput("HDMI-1");

        BrightnessControl lights = reg.getFirstOfType(BrightnessControl.class);
        lights.setBrightness(60);

        TemperatureControl ac = reg.getFirstOfType(TemperatureControl.class);
        ac.setTemperatureC(24);

        AttendanceControl scan = reg.getFirstOfType(AttendanceControl.class);
        System.out.println("Attendance scanned: present=" + scan.scanAttendance());
    }

    public void endClass() {
        System.out.println("Shutdown sequence:");
        reg.getFirstOfType(InputControl.class).powerOff();
        reg.getFirstOfType(BrightnessControl.class).powerOff();
        reg.getFirstOfType(TemperatureControl.class).powerOff();
    }
}
