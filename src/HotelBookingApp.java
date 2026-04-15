/**
 * =============================================================
 * MAIN CLASS - HotelBookingApp
 * =============================================================
 *
 * Use Case 2: Basic Room Types & Static Availability
 *
 * Description:
 * Demonstrates abstraction, inheritance, and basic room modeling
 * with static availability.
 *
 * @author Developer
 * @version 2.1
 */

// Abstract class
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
        System.out.println("Available: " + available + "\n");
    }
}

// Single Room
class SingleRoom extends Room {
    SingleRoom() {
        super(1, 250, 1500.0);
    }

    String getRoomType() {
        return "Single Room";
    }
}

// Double Room
class DoubleRoom extends Room {
    DoubleRoom() {
        super(2, 400, 2500.0);
    }

    String getRoomType() {
        return "Double Room";
    }
}

// Suite Room
class SuiteRoom extends Room {
    SuiteRoom() {
        super(3, 750, 5000.0);
    }

    String getRoomType() {
        return "Suite Room";
    }
}

// Main class
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("      Hotel Room Initialization        ");
        System.out.println("=======================================\n");

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display details
        single.displayDetails(singleAvailable);
        doubleRoom.displayDetails(doubleAvailable);
        suite.displayDetails(suiteAvailable);

        System.out.println("UC2 execution completed.");
    }
}