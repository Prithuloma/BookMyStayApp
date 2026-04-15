import java.util.*;

/**
 * =============================================================
 * MAIN CLASS - HotelBookingApp
 * =============================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * Processes booking queue, allocates rooms, prevents duplicates.
 *
 * @author Developer
 * @version 6.0
 */

// Reservation
class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Inventory Service
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

// Booking Service
class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomAllocations = new HashMap<>();
    private int idCounter = 1;

    void processBookings(Queue<Reservation> queue, RoomInventory inventory) {

        System.out.println("Processing Booking Requests:\n");

        while (!queue.isEmpty()) {

            Reservation r = queue.poll(); // FIFO

            int available = inventory.getAvailability(r.roomType);

            if (available > 0) {

                // Generate unique room ID
                String roomId = r.roomType.substring(0, 2).toUpperCase() + idCounter++;

                // Ensure uniqueness (extra safety)
                while (allocatedRoomIds.contains(roomId)) {
                    roomId = r.roomType.substring(0, 2).toUpperCase() + idCounter++;
                }

                // Store ID
                allocatedRoomIds.add(roomId);

                // Map room type → IDs
                roomAllocations
                        .computeIfAbsent(r.roomType, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory
                inventory.reduceRoom(r.roomType);

                System.out.println("Confirmed: " + r.guestName +
                        " | Room Type: " + r.roomType +
                        " | Room ID: " + roomId);

            } else {
                System.out.println("Rejected (No Availability): " +
                        r.guestName + " - " + r.roomType);
            }
        }
    }
}

// Main
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Reservation & Room Allocation       ");
        System.out.println("=======================================\n");

        // Inventory setup
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);
        inventory.addRoom("Double Room", 1);
        inventory.addRoom("Suite Room", 1);

        // Queue (from UC5)
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Alice", "Single Room"));
        queue.offer(new Reservation("Bob", "Single Room"));
        queue.offer(new Reservation("Charlie", "Single Room")); // will fail
        queue.offer(new Reservation("David", "Double Room"));
        queue.offer(new Reservation("Eve", "Suite Room"));

        // Booking service
        BookingService service = new BookingService();
        service.processBookings(queue, inventory);

        System.out.println("\nUC6 allocation completed.");
    }
}