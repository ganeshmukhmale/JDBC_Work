import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String url="jdbc:mysql://localhost:3306/JDBC";
        String user="root";
        String password="Ganeshmm@123";
        // establish credentials
        try (Connection connection= DriverManager.getConnection(url,user,password)){
            System.out.println("Connected to the Database.");
        } catch (SQLException e) {
            System.err.println("Connection Failed: "+ e.getMessage());
        }
    }
}