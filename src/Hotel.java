import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Hotel {
    private static final String url = "jdbc:mysql://localhost:3306/Hotel_Db"; // Removed extra space in URL
    private static final String user = "root";
    private static final String password = "Ganeshmm@123";

    public static void main(String[] args) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try (Connection con = DriverManager.getConnection(url, user, password)) { // Properly closed connection
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println();
                System.out.println("Hotel Management System");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline
                switch (choice) {
                    case 1:
                        reserveRoom(con, sc);
                        break;
                    case 2:
                        viewReservations(con);
                        break;
                    case 3:
                        getRoomNumber(con, sc);
                        break;
                    case 4:
                        updateReservation(con, sc);
                        break;
                    case 5:
                        deleteReservation(con, sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Guest name: ");
            String guestName = sc.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = sc.nextInt();
            sc.nextLine(); // Consume newline
            System.out.print("Enter contact number: ");
            String contactNumber = sc.nextLine();

            String sql = "INSERT INTO reservation (Guest_Name, Room_Number, Contact_Number) VALUES ('"
                    + guestName + "', " + roomNumber + ", '" + contactNumber + "')";
            try (Statement statement = con.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation Successful!");
                } else {
                    System.out.println("Reservation failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservations(Connection con) throws SQLException {
        String sql = "SELECT Reservation_Id, Guest_Name, Room_Number, Contact_Number, Reservation_Date FROM Reservation";

        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Current Reservations: ");
            System.out.println("+----------------+------------------------+-------------+----------------+--------------------+");
            System.out.println("| Reservation ID |       Guest Name       | Room Number | Contact Number |  Reservation Date  |");
            System.out.println("+----------------+------------------------+-------------+----------------+--------------------+");

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("Reservation_Id");
                String guestName = resultSet.getString("Guest_Name");
                int roomNumber = resultSet.getInt("Room_Number");
                String contactNumber = resultSet.getString("Contact_Number");
                String reservationDate = resultSet.getString("Reservation_Date");
                System.out.printf("| %-14d | %-22s | %-11d | %-14s | %-18s |\n", reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }
            System.out.println("+----------------+------------------------+-------------+----------------+--------------------+");
        }
    }

    private static void getRoomNumber(Connection con, Scanner sc) {
        try {
            System.out.print("Enter reservation ID: ");
            int reservationId = sc.nextInt();
            sc.nextLine(); // Consume newline
            System.out.print("Enter Guest Name: ");
            String guestName = sc.nextLine();

            String sql = "SELECT Room_Number FROM Reservation WHERE Reservation_Id = " + reservationId + " AND Guest_Name = '" + guestName + "'";

            try (Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("Room_Number");
                    System.out.println("Room number for Reservation ID: " + reservationId + " guest Name is: " + guestName + " Room Number is: " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Reservation ID to update: ");
            int reservationId = sc.nextInt();
            sc.nextLine(); // Consume newline

            if (!reservationExists(con, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }
            System.out.print("Enter new guest name: ");
            String newGuestName = sc.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = sc.nextInt();
            sc.nextLine(); // Consume newline
            System.out.print("Enter new contact number: ");
            String newContactNumber = sc.nextLine();

            String sql = "UPDATE Reservation SET Guest_Name = '" + newGuestName + "', Room_Number = " + newRoomNumber + ", Contact_Number = '" + newContactNumber + "' WHERE Reservation_Id = " + reservationId;

            try (Statement statement = con.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Reservation ID to delete: ");
            int reservationId = sc.nextInt();

            if (!reservationExists(con, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }
            String sql = "DELETE FROM Reservation WHERE Reservation_Id = " + reservationId;

            try (Statement statement = con.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection con, int reservationId) {
        try {
            String sql = "SELECT Reservation_Id FROM Reservation WHERE Reservation_Id = " + reservationId;

            try (Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while (i != 0) {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using the Hotel Reservation System!");
    }
}
