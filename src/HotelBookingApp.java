import java.io.*;
import java.util.*;

/**
 * =============================================================
 * MAIN CLASS - HotelBookingApp
 * =============================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * Saves and restores inventory + booking state using serialization.
 *
 * @author Developer
 * @version 12.0
 */

// ---------- Reservation ----------
class Reservation implements Serializable {
    String reservationId;
    String guestName;
    String roomType;
    String roomId;

    Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }
}

// ---------- Inventory ----------
class RoomInventory implements Serializable {
    Map<String, Integer> inventory = new HashMap<>();

    void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    void reduceRoom(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    Map<String, Integer> getAll() {
        return inventory;
    }
}

// ---------- System State (NEW) ----------
class SystemState implements Serializable {
    RoomInventory inventory;
    List<Reservation> bookings;

    SystemState(RoomInventory inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// ---------- Persistence Service ----------
class PersistenceService {

    private static final String FILE_NAME = "hotel_state.ser";

    // SAVE
    void save(SystemState state) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("State saved successfully.");

        } catch (IOException e) {
            System.out.println("ERROR saving state: " + e.getMessage());
        }
    }

    // LOAD
    SystemState load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("State loaded successfully.");
            return (SystemState) in.readObject();

        } catch (Exception e) {
            System.out.println("No previous state found. Starting fresh.");
            return null;
        }
    }
}

// ---------- Main ----------
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Data Persistence & Recovery (UC12)  ");
        System.out.println("=======================================\n");

        PersistenceService persistence = new PersistenceService();

        // Try loading previous state
        SystemState state = persistence.load();

        RoomInventory inventory;
        List<Reservation> bookings;

        if (state != null) {
            inventory = state.inventory;
            bookings = state.bookings;

            System.out.println("\nRecovered Data:");
            for (Reservation r : bookings) {
                System.out.println(r.reservationId + " | " +
                        r.guestName + " | " +
                        r.roomType + " | " + r.roomId);
            }

        } else {
            // Fresh start
            inventory = new RoomInventory();
            inventory.addRoom("Single Room", 2);

            bookings = new ArrayList<>();

            // Simulate booking
            Reservation r1 = new Reservation("RES1", "Alice", "Single Room", "SI1");
            bookings.add(r1);
            inventory.reduceRoom("Single Room");

            System.out.println("New booking created: " + r1.guestName);
        }

        // Save state before exit
        SystemState newState = new SystemState(inventory, bookings);
        persistence.save(newState);

        System.out.println("\nUC12 persistence completed.");
    }
}