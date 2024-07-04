package SQL_Connection;

import java.sql.*;


public class SQL_Connector {
    static Connection connection;
    static String URL = "jdbc:mysql://localhost:3307/";
    static String DATABASE = "shipping_lines_ticketing";
    static String USER = "root";
    static String PASSWORD = "Schoolpurposes";
    
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL + DATABASE, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return connection;
    }
}