import java.util.HashMap;
import java.util.Map;

/**
 * =============================================================
 * MAIN CLASS - HotelBookingApp
 * =============================================================
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * Description:
 * Uses HashMap to manage room availability centrally.
 *
 * @author Developer
 * @version 3.1
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

// ✅ Inventory Class (NEW CONCEPT)
class RoomInventory {
    private HashMap<String, Integer> inventory;

    // Constructor
    RoomInventory() {
        inventory = new HashMap<>();
    }

    // Add room type
    void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    // Get availability
    int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Display all inventory
    void displayInventory(Map<String, Room> roomMap) {
        for (String type : inventory.keySet()) {
            Room room = roomMap.get(type);
            int available = inventory.get(type);
            room.displayDetails(available);
        }
    }
}

// Main Class
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Hotel Room Inventory Status         ");
        System.out.println("=======================================\n");

        // Create rooms
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Map room types → objects
        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put("Single Room", single);
        roomMap.put("Double Room", doubleRoom);
        roomMap.put("Suite Room", suite);

        // Create inventory
        RoomInventory inventory = new RoomInventory();

        // Add availability
        inventory.addRoom("Single Room", 5);
        inventory.addRoom("Double Room", 3);
        inventory.addRoom("Suite Room", 2);

        // Display
        inventory.displayInventory(roomMap);

        System.out.println("UC3 execution completed.");
    }
}