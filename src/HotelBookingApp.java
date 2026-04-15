import java.util.*;

/**
 * =============================================================
 * MAIN CLASS - HotelBookingApp
 * =============================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * Stores confirmed bookings and generates reports.
 *
 * @author Developer
 * @version 8.0
 */

// ---------- Reservation ----------
class Reservation {
    String reservationId;
    String guestName;
    String roomType;
    String roomId;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// ---------- Inventory ----------
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    void reduceRoom(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}

// ---------- Booking History (NEW) ----------
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    void addReservation(Reservation r) {
        history.add(r); // preserves order
    }

    List<Reservation> getAllBookings() {
        return history; // read-only usage expected
    }
}

// ---------- Booking Service ----------
class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private int idCounter = 1;

    void processBookings(Queue<Reservation> queue,
                         RoomInventory inventory,
                         BookingHistory history) {

        System.out.println("Processing Bookings:\n");

        while (!queue.isEmpty()) {

            Reservation r = queue.poll();

            int available = inventory.getAvailability(r.roomType);

            if (available > 0) {

                // Generate room ID
                String roomId = r.roomType.substring(0, 2).toUpperCase() + idCounter++;
                while (allocatedRoomIds.contains(roomId)) {
                    roomId = r.roomType.substring(0, 2).toUpperCase() + idCounter++;
                }

                allocatedRoomIds.add(roomId);

                // Assign details
                r.roomId = roomId;
                r.reservationId = "RES" + idCounter;

                // Update inventory
                inventory.reduceRoom(r.roomType);

                // ✅ Add to history
                history.addReservation(r);

                System.out.println("Confirmed: " + r.guestName +
                        " | " + r.roomType +
                        " | Room ID: " + roomId +
                        " | Reservation ID: " + r.reservationId);

            } else {
                System.out.println("Rejected: " + r.guestName +
                        " (" + r.roomType + " unavailable)");
            }
        }
    }
}

// ---------- Report Service (NEW) ----------
class BookingReportService {

    void displayAllBookings(List<Reservation> history) {

        System.out.println("\n===== Booking History =====\n");

        for (Reservation r : history) {
            System.out.println(r.reservationId + " | " +
                    r.guestName + " | " +
                    r.roomType + " | Room ID: " + r.roomId);
        }
    }

    void generateSummary(List<Reservation> history) {

        System.out.println("\n===== Booking Summary =====\n");

        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : history) {
            summary.put(r.roomType,
                    summary.getOrDefault(r.roomType, 0) + 1);
        }

        for (String type : summary.keySet()) {
            System.out.println(type + " booked: " + summary.get(type));
        }
    }
}

// ---------- Main ----------
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Booking History & Reporting (UC8)   ");
        System.out.println("=======================================\n");

        // Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);
        inventory.addRoom("Double Room", 1);

        // Queue
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Alice", "Single Room"));
        queue.offer(new Reservation("Bob", "Single Room"));
        queue.offer(new Reservation("Charlie", "Double Room"));

        // History
        BookingHistory history = new BookingHistory();

        // Booking
        BookingService bookingService = new BookingService();
        bookingService.processBookings(queue, inventory, history);

        // Reporting
        BookingReportService reportService = new BookingReportService();
        reportService.displayAllBookings(history.getAllBookings());
        reportService.generateSummary(history.getAllBookings());

        System.out.println("\nUC8 reporting completed.");
    }
}