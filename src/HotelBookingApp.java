import java.util.*;

/**
 * =============================================================
 * MAIN CLASS - HotelBookingApp
 * =============================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * Description:
 * Attaches optional services to reservations without changing
 * booking/allocation logic.
 *
 * @author Developer
 * @version 7.0
 */

// ---------- Reservation ----------
class Reservation {
    String guestName;
    String roomType;
    String reservationId; // assigned at confirmation

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

// ---------- Booking Service (from UC6, slightly adapted to return IDs) ----------
class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private int idCounter = 1;

    // returns reservationId → roomId mapping
    Map<String, String> processBookings(Queue<Reservation> queue, RoomInventory inventory) {

        Map<String, String> confirmed = new LinkedHashMap<>();

        System.out.println("Processing Booking Requests:\n");

        while (!queue.isEmpty()) {
            Reservation r = queue.poll();

            int available = inventory.getAvailability(r.roomType);

            if (available > 0) {

                // generate unique room ID
                String roomId = r.roomType.substring(0, 2).toUpperCase() + idCounter++;
                while (allocatedRoomIds.contains(roomId)) {
                    roomId = r.roomType.substring(0, 2).toUpperCase() + idCounter++;
                }
                allocatedRoomIds.add(roomId);

                // create reservationId (simple)
                String reservationId = "RES" + idCounter;
                r.reservationId = reservationId;

                // update inventory
                inventory.reduceRoom(r.roomType);

                confirmed.put(reservationId, roomId);

                System.out.println("Confirmed: " + r.guestName +
                        " | " + r.roomType +
                        " | Room ID: " + roomId +
                        " | Reservation ID: " + reservationId);

            } else {
                System.out.println("Rejected (No Availability): " +
                        r.guestName + " - " + r.roomType);
            }
        }
        return confirmed;
    }
}

// ---------- Add-On Service ----------
class AddOnService {
    String name;
    double price;

    AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

// ---------- Service Manager (NEW) ----------
class AddOnServiceManager {

    // reservationId → list of services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // attach service
    void addService(String reservationId, AddOnService service) {
        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // calculate cost
    double getTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());

        double total = 0;
        for (AddOnService s : services) {
            total += s.price;
        }
        return total;
    }

    // display services
    void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());

        System.out.println("\nServices for " + reservationId + ":");

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println("- " + s.name + " : " + s.price);
        }

        System.out.println("Total Add-On Cost: " + getTotalCost(reservationId));
    }
}

// ---------- Main ----------
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Add-On Service Selection (UC7)      ");
        System.out.println("=======================================\n");

        // Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);
        inventory.addRoom("Double Room", 1);

        // Queue
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Alice", "Single Room"));
        queue.offer(new Reservation("Bob", "Double Room"));

        // Booking
        BookingService bookingService = new BookingService();
        Map<String, String> confirmed = bookingService.processBookings(queue, inventory);

        // Add-on manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Add services (example)
        for (String resId : confirmed.keySet()) {

            manager.addService(resId, new AddOnService("Breakfast", 300));
            manager.addService(resId, new AddOnService("Airport Pickup", 800));

            manager.displayServices(resId);
        }

        System.out.println("\nUC7 add-on processing completed.");
    }
}