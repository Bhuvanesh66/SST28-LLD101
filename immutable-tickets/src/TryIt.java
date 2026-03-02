import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;

import java.util.List;

/**
 * Starter demo that shows why mutability is risky.
 *
 * After refactor:
 * - direct mutation should not compile (no setters)
 * - external modifications to tags should not affect the ticket
 * - service "updates" should return a NEW ticket instance
 */
public class TryIt {

    public static void main(String[] args) {
        TicketService service = new TicketService();

        IncidentTicket t = service.createTicket("TCK-1001", "reporter@example.com", "Payment failing on checkout");
        System.out.println("Created:\n " + t);

        // Demonstrate post-creation mutation through service
        IncidentTicket newt = service.assign(t, "agent@example.com");
        newt = service.escalateToCritical(newt);
        System.out.println("\nOriginal after service mutations:\n " + t);
        System.out.println("\nNew ticket after service mutations:\n " + newt);

        // Demonstrate external mutation via leaked list reference
        List<String> tags = newt.getTags();
        tags.add("HACKED_FROM_OUTSIDE");
        System.out.println("\nAfter external tag mutation:\n " + newt);

        // Starter compiles; after refactor, you should redesign updates to create new objects instead.
    }
}