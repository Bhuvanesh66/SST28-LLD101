import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Hostel Fee Calculator ===");

        Map<Integer, RoomPricing> roomPricingMap = new HashMap<>();
        roomPricingMap.put(LegacyRoomTypes.SINGLE, new SingleRoom());
        roomPricingMap.put(LegacyRoomTypes.DOUBLE, new DoubleRoom());
        roomPricingMap.put(LegacyRoomTypes.TRIPLE, new TripleRoom());
        roomPricingMap.put(LegacyRoomTypes.DELUXE, new DeluxeRoom());

        BookingRequest req = new BookingRequest(LegacyRoomTypes.DOUBLE, List.of(AddOn.LAUNDRY, AddOn.MESS));
        HostelFeeCalculator calc = new HostelFeeCalculator(new FakeBookingRepo(), roomPricingMap);
        calc.process(req);
    }
}
