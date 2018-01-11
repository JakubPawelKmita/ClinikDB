package Core;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    String actualUser;
    public Connection(String actualUser) {
        this.actualUser = actualUser;
    }

    public java.sql.Connection connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306", actualUser, actualUser+"pwd");
            System.out.println("Connected succesfully");
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
