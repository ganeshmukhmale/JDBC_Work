// We Retrieve Data with the help of Database ;

import java.sql.*;

public class Main1 {
    public static void main(String[] args) throws ClassNotFoundException{

        String url="jdbc:mysql://localhost:3306/JDBC"; // Our Database Path
        String user="root";
        String password="Ganeshmm@123";
        String Query="Select * from Employee;";

        // Load Drivers
        try {
            Class.forName("com.mysql.jdbc.Driver"); // load drivers that is present in com.mysql package
            System.out.println("Drivers loaded Successfully!!");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // Establish Connection
        try {
            Connection con= DriverManager.getConnection(url,user,password);
            // DriverManager is the class it has getConnection method and take 3 input or argument (url,user,password) that all thing store in con refrence and con is instance of Connection interface.
            System.out.println("Connection Established Successfully !!");

            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(Query); // stmt.executeQuery(Query) hold query and it stores inside the Resultset interface reference that is (rs).
            /*
            con.createStatement():
            1. This method creates a Statement object.
            2.A Statement is used to send SQL commands (like queries or updates) to the database.
            stmt :
            1. This is a variable that holds the Statement object returned by the method.
            2. You will use this stm object to execute SQL commands.
            */
            while (rs.next()){
                int ID=rs.getInt("ID");
                String Name=rs.getString("Name");
                String Job_title=rs.getString("Job_title");
                double Salary=rs.getDouble("Salary");
                System.out.println("--------------------------------");
                System.out.println("ID: "+ID);
                System.out.println("Name: "+Name);
                System.out.println("Job_title: "+Job_title);
                System.out.println("Salary: "+Salary);
            }
            rs.close();
            stmt.close();
            con.close();
            System.out.println();
            System.out.println("Connection close Successfully!!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
