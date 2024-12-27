// How to insert data in database with the help of java.

import java.sql.*;

public class Main2 {
    public static void main(String[] args) throws ClassNotFoundException{
        String url="jdbc:mysql://localhost:3306/JDBC"; // Our Database Path
        String user="root";
        String password="Ganeshmm@123";
        String Query="insert into Employee values(0,'Rohan Deshmukh','Hacker',50000),(0,'Bharat Puri','Python Developer',56000);";

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
            int rowsaffected =stmt.executeUpdate(Query); // stmt.executeQuery(Query) hold query and it stores inside the Resultset interface reference that is (rs).

            /*
            con.createStatement():
            1. This method creates a Statement object.
            2.A Statement is used to send SQL commands (like queries or updates) to the database.
            stmt :
            1. This is a variable that holds the Statement object returned by the method.
            2. You will use this stm object to execute SQL commands.
            */
            if (rowsaffected>0){
                System.out.println("Insert Successfully. "+rowsaffected+ " row's affected.");
            }
            else {
                System.out.println("Data not Inserted!");
            }
            stmt.close();
            con.close();
            System.out.println();
            System.out.println("Connection close Successfully!!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
