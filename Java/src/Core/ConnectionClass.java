package Core;

import javafx.scene.control.Label;

import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class ConnectionClass {
    private String actualUser;
    private Connection con;
    private String pwd;
    private String url = "jdbc:mysql://localhost:3306";
    public ConnectionClass(String actualUser) {
        this.actualUser = actualUser;
        System.out.println(actualUser);
    }

    public java.sql.Connection connect() {
        try {
            FileInputStream fstream = new FileInputStream(actualUser + ".txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            pwd = br.readLine();
            System.out.println(pwd);
            br.close();
            fstream.close();
            Class.forName("com.mysql.jdbc.Driver");
            //java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clinicdb", "root", "kopytko");
            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clinicdb", actualUser, pwd);
            System.out.println("Connected succesfully");
            this.con = con;
            return con;
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (NullPointerException e) {}
        return null;
    }

    public void viewTable(java.sql.Connection con, String dbName) throws SQLException {

        Statement stmt = null;
        String query =
                "select name, surname " +
                        "from " + dbName + ".doctors";

        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                String surname = rs.getString("surname");

                System.out.println(name + " " + surname + "\t");
            }
        } catch (SQLException e ) {
            System.out.println("nie masz uprawnień!!!");

        } catch (NullPointerException e) {}
        finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    public java.sql.Connection getConnectionRef() {
        return con;
    }

    public String getConName() {
        return actualUser;
    }

    public String getPwd() {
        return pwd;
    }

    public String getUrl() {
        return url;
    }

    public String getActualUser(){
        return  actualUser;
    }
}
