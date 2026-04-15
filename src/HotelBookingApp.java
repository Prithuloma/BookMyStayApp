import java.util.*;

/**
 * =============================================================
 * MAIN CLASS - HotelBookingApp
 * =============================================================
 *
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 *
 * Description:
 * Simulates concurrent booking requests using threads and
 * ensures safe allocation using synchronization.
 *
 * @author Developer
 * @version 11.0
 */

// ---------- Reservation ----------
class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// ---------- Inventory (Thread-safe methods) ----------
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    // synchronized → critical section
    synchronized boolean allocateRoom(String type) {

        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        }
        return false;
    }

    synchronized int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// ---------- Shared Queue ----------
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    synchronized void add(Reservation r) {
        queue.offer(r);
    }

    synchronized Reservation get() {
        return queue.poll();
    }
}

// ---------- Worker Thread ----------
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;
    private static int idCounter = 1;

    BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            Reservation r;

            // synchronized access to queue
            synchronized (queue) {
                r = queue.get();
            }

            if (r == null) break;

            // critical section for allocation
            boolean success = inventory.allocateRoom(r.roomType);

            if (success) {
                String roomId = r.roomType.substring(0, 2).toUpperCase() + (idCounter++);

                System.out.println(Thread.currentThread().getName() +
                        " confirmed " + r.guestName +
                        " | " + r.roomType +
                        " | Room ID: " + roomId);
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " rejected " + r.guestName +
                        " (No availability)");
            }
        }
    }
}

// ---------- Main ----------
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Concurrent Booking Simulation       ");
        System.out.println("=======================================\n");

        // Shared inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);

        // Shared queue
        BookingQueue queue = new BookingQueue();
        queue.add(new Reservation("Alice", "Single Room"));
        queue.add(new Reservation("Bob", "Single Room"));
        queue.add(new Reservation("Charlie", "Single Room"));

        // Multiple threads
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);

        t1.start();
        t2.start();

        // wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nRemaining Rooms: " +
                inventory.getAvailability("Single Room"));

        System.out.println("\nUC11 concurrency completed.");
    }
}