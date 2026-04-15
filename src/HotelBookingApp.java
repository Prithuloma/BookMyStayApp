import java.util.*;

/**
 * =============================================================
 * MAIN CLASS - HotelBookingApp
 * =============================================================
 *
 * Use Case 5: Booking Request (First-Come-First-Served)
 *
 * Description:
 * Stores booking requests using Queue (FIFO) without allocation.
 *
 * @author Developer
 * @version 5.0
 */

// Room (same as before)
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

// ✅ Reservation (NEW)
class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println(guestName + " requested " + roomType);
    }
}

// ✅ Booking Queue (NEW)
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // Add request
    void addRequest(Reservation r) {
        queue.offer(r); // FIFO insert
    }

    // Display all requests
    void displayQueue() {
        System.out.println("Booking Requests (FIFO Order):\n");

        for (Reservation r : queue) {
            r.display();
        }
    }
}

// Main
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Booking Request Queue (UC5)         ");
        System.out.println("=======================================\n");

        // Create queue
        BookingQueue bookingQueue = new BookingQueue();

        // Add booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));
        bookingQueue.addRequest(new Reservation("David", "Single Room"));

        // Display queue
        bookingQueue.displayQueue();

        System.out.println("\nUC5 request intake completed.");
    }
}