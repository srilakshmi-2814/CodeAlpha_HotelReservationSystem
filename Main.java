import java.io.*;
import java.util.*;
class Room {
    int roomId;
    String category;
    boolean isAvailable;
    Room(int roomId, String category, boolean isAvailable) {
        this.roomId = roomId;
        this.category = category;
        this.isAvailable = isAvailable;
    }
}
class Booking {
    int bookingId;
    String customerName;
    int roomId;
    String category;
    Booking(int bookingId, String customerName, int roomId, String category) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomId = roomId;
        this.category = category;
    }
}
class HotelSystem {
    static List<Room> rooms = new ArrayList<>();
    static List<Booking> bookings = new ArrayList<>();
    static int bookingCounter = 1;
    static void initializeRooms() {
        rooms.add(new Room(101, "Standard", true));
        rooms.add(new Room(102, "Deluxe", true));
        rooms.add(new Room(103, "Suite", true));
        rooms.add(new Room(104, "Standard", true));
        rooms.add(new Room(105, "Deluxe", true));
    }
    static void searchRooms(String category) {
        System.out.println("\nAvailable " + category + " Rooms:");
        for (Room r : rooms) {
            if (r.category.equalsIgnoreCase(category) && r.isAvailable) {
                System.out.println("Room ID: " + r.roomId);
            }
        }
    }
    static void bookRoom(String name, String category) {
        for (Room r : rooms) {
            if (r.category.equalsIgnoreCase(category) && r.isAvailable) {
                r.isAvailable = false;
                Booking b = new Booking(bookingCounter++, name, r.roomId, category);
                bookings.add(b);
                saveToFile();
                System.out.println("\nPayment Successful!");
                System.out.println("Booking Confirmed!");
                System.out.println("Booking ID: " + b.bookingId);
                System.out.println("Room ID: " + r.roomId);
                return;
            }
        }
        System.out.println("No rooms available!");
    }
    static void cancelBooking(int bookingId) {
        Iterator<Booking> it = bookings.iterator();
        while (it.hasNext()) {
            Booking b = it.next();
            if (b.bookingId == bookingId) {
                for (Room r : rooms) {
                    if (r.roomId == b.roomId) {
                        r.isAvailable = true;
                    }
                }
                it.remove();
                saveToFile();
                System.out.println("Booking Cancelled!");
                return;
            }
        }
        System.out.println("Booking not found!");
    }
    static void viewBookings() {
        System.out.println("\nAll Bookings:");
        for (Booking b : bookings) {
            System.out.println("ID: " + b.bookingId +
                    ", Name: " + b.customerName +
                    ", Room: " + b.roomId +
                    ", Category: " + b.category);
        }
    }
    static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("bookings.txt"))) {
            for (Booking b : bookings) {
                pw.println(b.bookingId + "," + b.customerName + "," + b.roomId + "," + b.category);
            }
        } catch (IOException e) {
            System.out.println("Error saving data!");
        }
    }
    static void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                bookings.add(new Booking(
                        Integer.parseInt(data[0]),
                        data[1],
                        Integer.parseInt(data[2]),
                        data[3]
                ));
            }
        } catch (IOException e) {
            // file may not exist initially
        }
    }
}
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HotelSystem.initializeRooms();
        HotelSystem.loadFromFile();
        while (true) {
            System.out.println("\n1. Search Room");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Bookings");
            System.out.println("5. Exit");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter category (Standard/Deluxe/Suite): ");
                    String cat = sc.nextLine();
                    HotelSystem.searchRooms(cat);
                    break;
                case 2:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter category: ");
                    String category = sc.nextLine();
                    HotelSystem.bookRoom(name, category);
                    break;
                case 3:
                    System.out.print("Enter Booking ID: ");
                    int id = sc.nextInt();
                    HotelSystem.cancelBooking(id);
                    break;
                case 4:
                    HotelSystem.viewBookings();
                    break;
                case 5:
                    System.exit(0);
            }
        }
    }
}