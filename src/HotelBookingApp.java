import java.util.HashMap;
import java.util.Map;

/**
 * =============================================================
 * MAIN CLASS - HotelBookingApp
 * =============================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * Description:
 * Allows users to search available rooms without modifying state.
 *
 * @author Developer
 * @version 4.0
 */

// Abstract Room
abstract class Room {
    int beds;
    int size;
    double price;

    Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    abstract String getRoomType();

    void displayDetails(int available) {
        System.out.println(getRoomType() + ":");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
        System.out.println("Available Rooms: " + available + "\n");
    }
}

// Room Types
class SingleRoom extends Room {
    SingleRoom() { super(1, 250, 1500.0); }
    String getRoomType() { return "Single Room"; }
}

class DoubleRoom extends Room {
    DoubleRoom() { super(2, 400, 2500.0); }
    String getRoomType() { return "Double Room"; }
}

class SuiteRoom extends Room {
    SuiteRoom() { super(3, 750, 5000.0); }
    String getRoomType() { return "Suite Room"; }
}

// Inventory (READ ONLY used here)
class RoomInventory {
    private HashMap<String, Integer> inventory = new HashMap<>();

    void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // expose read-only map (safe usage)
    Map<String, Integer> getAllRooms() {
        return inventory;
    }
}

// ✅ Search Service (NEW CONCEPT)
class RoomSearchService {

    void searchAvailableRooms(RoomInventory inventory, Map<String, Room> roomMap) {

        System.out.println("Available Rooms:\n");

        for (String type : inventory.getAllRooms().keySet()) {

            int available = inventory.getAvailability(type);

            // ✅ filter unavailable rooms
            if (available > 0) {
                Room room = roomMap.get(type);
                room.displayDetails(available);
            }
        }
    }
}

// Main Class
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("     Room Search & Availability        ");
        System.out.println("=======================================\n");

        // Create rooms
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put("Single Room", single);
        roomMap.put("Double Room", doubleRoom);
        roomMap.put("Suite Room", suite);

        // Inventory setup
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 5);
        inventory.addRoom("Double Room", 3);
        inventory.addRoom("Suite Room", 2);
        // try putting 0 to test filtering:
        // inventory.addRoom("Suite Room", 0);

        // ✅ Search (read-only)
        RoomSearchService searchService = new RoomSearchService();
        searchService.searchAvailableRooms(inventory, roomMap);

        System.out.println("UC4 search completed.");
    }
}