import java.util.*;

/**
 * =============================================================
 * MAIN CLASS - HotelBookingApp
 * =============================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * Supports cancellation with rollback using Stack (LIFO).
 *
 * @author Developer
 * @version 10.0
 */

// ---------- Reservation ----------
class Reservation {
    String reservationId;
    String guestName;
    String roomType;
    String roomId;
    boolean isCancelled = false;

    Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
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

    void increaseRoom(String type) {
        inventory.put(type, inventory.get(type) + 1);
    }
}

// ---------- Booking History ----------
class BookingHistory {
    private Map<String, Reservation> history = new HashMap<>();

    void add(Reservation r) {
        history.put(r.reservationId, r);
    }

    Reservation get(String id) {
        return history.get(id);
    }

    Collection<Reservation> getAll() {
        return history.values();
    }
}

// ---------- Cancellation Service ----------
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    void cancel(String reservationId,
                BookingHistory history,
                RoomInventory inventory) {

        System.out.println("\nProcessing Cancellation for: " + reservationId);

        Reservation r = history.get(reservationId);

        // Validate
        if (r == null) {
            System.out.println("ERROR: Reservation does not exist.");
            return;
        }

        if (r.isCancelled) {
            System.out.println("ERROR: Reservation already cancelled.");
            return;
        }

        // Push to stack (rollback tracking)
        rollbackStack.push(r.roomId);

        // Restore inventory
        inventory.increaseRoom(r.roomType);

        // Mark cancelled
        r.isCancelled = true;

        System.out.println("Cancelled Successfully: " + r.guestName +
                " | Room ID released: " + r.roomId);
    }

    void showRollbackHistory() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

// ---------- Main ----------
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Booking Cancellation & Rollback     ");
        System.out.println("=======================================\n");

        // Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 1);

        // Simulate confirmed booking
        Reservation r1 = new Reservation("RES1", "Alice", "Single Room", "SI1");

        // Reduce inventory (booking happened earlier)
        inventory.reduceRoom("Single Room");

        // Store in history
        BookingHistory history = new BookingHistory();
        history.add(r1);

        // Cancellation service
        CancellationService service = new CancellationService();

        // Valid cancellation
        service.cancel("RES1", history, inventory);

        // Duplicate cancellation attempt
        service.cancel("RES1", history, inventory);

        // Invalid cancellation
        service.cancel("RES99", history, inventory);

        // Show rollback stack
        service.showRollbackHistory();

        System.out.println("\nUC10 rollback completed.");
    }
}