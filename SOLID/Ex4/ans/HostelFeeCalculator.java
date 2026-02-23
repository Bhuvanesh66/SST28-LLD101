import java.util.*;

public class HostelFeeCalculator {
    private final FakeBookingRepo repo;
    private final Map<Integer, RoomPricing> roomPricingMap;

    public HostelFeeCalculator(FakeBookingRepo repo, Map<Integer, RoomPricing> roomPricingMap) {
        this.repo = repo;
        this.roomPricingMap = roomPricingMap;
    }

    public void process(BookingRequest req) {
        Money monthly = calculateMonthly(req);
        Money deposit = new Money(5000.00);

        ReceiptPrinter.print(req, monthly, deposit);

        String bookingId = "H-" + (7000 + new Random(1).nextInt(1000));
        repo.save(bookingId, req, monthly, deposit);
    }

    private Money calculateMonthly(BookingRequest req) {
        RoomPricing room = roomPricingMap.getOrDefault(req.roomType, new DeluxeRoom());
        double base = room.monthlyFee();

        double addOnTotal = 0.0;
        for (AddOn a : req.addOns) {
            addOnTotal += a.price();
        }

        return new Money(base + addOnTotal);
    }
}
