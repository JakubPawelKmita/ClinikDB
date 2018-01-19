package Gui.Patient;

import com.mysql.jdbc.exceptions.MySQLDataException;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

public class AddVisit extends Application {
    public static Stage window = new Stage();
    private Scene scene;
    private Connection con;
    private Button addVisit;
    private TextField pesel, docName, docSurname, time, date;
    private Label Lpesel, LdocName, LdocSurname, Ltime, Ldate, label;
    private String spesel;
    private String sdocName;
    private String sdocSurname;
    private long stime;
    private String sdate;
    private String docID;

    public static void main(String[] args) {
        launch(args);
    }

    public AddVisit(Connection con) {
        this.con = con;
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        addVisit = new Button("Add visit");
        addVisit.setOnAction(e -> {
            try {
                setVisit();
            } catch (SQLException e1) {
                label.setText("Nie wpisałeś wszystkich danych!");
            } catch (NullPointerException ef) {
                label.setText("Nie wpisałeś wszystkich danych");
            } catch (NumberFormatException nm) {
                label.setText("Wprowadziłeś błędne dane (zły format, nieprawidłowe dane)");
            } catch (ArrayIndexOutOfBoundsException eg) {
                label.setText("Nie wpisałeś wszystkich danych, bądź wprowadziłeś nieprawidłowe!");
            }
        });

        pesel = new TextField();
        docName = new TextField();
        docSurname = new TextField();
        time = new TextField();
        date = new TextField();

        label = new Label("");
        Lpesel = new Label("Enter your pesel");
        LdocName = new Label("Enter doc's name");
        LdocSurname = new Label("Enter doc's surname");
        Ltime = new Label("Enter the visit's hour in format hh:mm");
        Ldate = new Label("enter the visit's date in format YYYY-MM-DD");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(Lpesel, pesel, LdocName, docName, LdocSurname, docSurname, Ltime, time, Ldate, date, addVisit, label);
        scene = new Scene(layout, 500, 500);
        window.setScene(scene);

        window.show();

    }

    private void setVisit() throws SQLException {
        spesel = pesel.getText();
        sdocName = docName.getText();
        sdocSurname = docSurname.getText();
        String[] split = time.getText().split(":");
        stime = Long.parseLong(split[0]) * 3600000 + Long.parseLong(split[1]) * 60000 - 3600000;
        sdate = date.getText();
        docID = getDocId();
        try {
            try {
                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(sdate);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                java.sql.Time sqlTime = new java.sql.Time(stime);
                System.out.println(sqlTime);
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO visits (visits.Doctor, visits.Patient, visits.time, visits.date) " +
                        "VALUES (?, ?, ?, ?)");
                pstmt.setString(1, docID);
                pstmt.setString(2, spesel);
                pstmt.setTime(3, sqlTime);
                pstmt.setDate(4, sqlDate);
                pstmt.execute();
                System.out.println("dodano wizytę");
            } catch (ParseException e) {
                label.setText("Nie wpisałeś wszystkich danych");
            }
            }  catch (MySQLIntegrityConstraintViolationException e) {
                label.setText("Wpisałeś błędny pesel");
            } catch (java.lang.NumberFormatException e) {
                label.setText("Nie wpisałeś wszystkich danych");
            } catch (MySQLDataException e) {

            } catch (ArrayIndexOutOfBoundsException e) {
                label.setText("Nie wpisałeś wszystkich danych, bądź wprowadziłeś nieprawidłowe!");
        }

    }

    private String getDocId() throws SQLException {
        Statement stmt = null;
        String query =
                "select PWZ " +
                        "from " + "clinicdb" + ".doctors " +
                        "WHERE " + "clinicdb.doctors.name = '" + sdocName + "'" + " AND " + "clinicdb.doctors.surname = '" + sdocSurname + "'";
        stmt = con.createStatement();
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
                docID = rs.getString("PWZ");
                return docID;
        }
        catch (SQLException e) {
            System.out.println("błąd przy ściąganiu ID lekarza");
        }
        return null;
    }
}
