import java.util.*;

/**
 * =============================================================
 * MAIN CLASS - HotelBookingApp
 * =============================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * Adds validation and custom exception handling to ensure
 * safe booking operations.
 *
 * @author Developer
 * @version 9.0
 */

// ---------- Custom Exception ----------
class InvalidBookingException extends Exception {
    InvalidBookingException(String message) {
        super(message);
    }
}

// ---------- Reservation ----------
class Reservation {
    String guestName;
    String roomType;

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
        return inventory.getOrDefault(type, -1);
    }

    void reduceRoom(String type) throws InvalidBookingException {
        int current = inventory.getOrDefault(type, -1);

        if (current <= 0) {
            throw new InvalidBookingException("No rooms available for: " + type);
        }

        inventory.put(type, current - 1);
    }

    boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }
}

// ---------- Validator (NEW) ----------
class BookingValidator {

    void validate(Reservation r, RoomInventory inventory) throws InvalidBookingException {

        // Validate room type
        if (!inventory.isValidRoomType(r.roomType)) {
            throw new InvalidBookingException("Invalid room type: " + r.roomType);
        }

        // Validate availability
        if (inventory.getAvailability(r.roomType) <= 0) {
            throw new InvalidBookingException("Room not available: " + r.roomType);
        }

        // Validate guest name
        if (r.guestName == null || r.guestName.isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }
    }
}

// ---------- Booking Service ----------
class BookingService {

    private int idCounter = 1;

    void processBookings(Queue<Reservation> queue,
                         RoomInventory inventory,
                         BookingValidator validator) {

        System.out.println("Processing Bookings:\n");

        while (!queue.isEmpty()) {

            Reservation r = queue.poll();

            try {
                // ✅ FAIL-FAST validation
                validator.validate(r, inventory);

                // Generate ID
                String roomId = r.roomType.substring(0, 2).toUpperCase() + idCounter++;

                // Update inventory safely
                inventory.reduceRoom(r.roomType);

                System.out.println("Confirmed: " + r.guestName +
                        " | " + r.roomType +
                        " | Room ID: " + roomId);

            } catch (InvalidBookingException e) {
                // ✅ Graceful failure
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }
}

// ---------- Main ----------
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Error Handling & Validation (UC9)   ");
        System.out.println("=======================================\n");

        // Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 1);
        inventory.addRoom("Double Room", 1);

        // Queue (includes invalid cases)
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Alice", "Single Room"));   // valid
        queue.offer(new Reservation("Bob", "Suite Room"));      // invalid type
        queue.offer(new Reservation("", "Double Room"));        // invalid name
        queue.offer(new Reservation("Charlie", "Single Room")); // no availability

        // Validator
        BookingValidator validator = new BookingValidator();

        // Booking
        BookingService service = new BookingService();
        service.processBookings(queue, inventory, validator);

        System.out.println("\nUC9 validation completed.");
    }
}