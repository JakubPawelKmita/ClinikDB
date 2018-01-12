package Gui.Patient;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewDoctorSchedule extends Application {
    public static Stage window = new Stage();
    private Scene scene;
    private Connection con;
    private String docName, docSurname;
    public static void main(String[] args) {
        launch(args);
    }

    public ViewDoctorSchedule(java.sql.Connection con, String docName, String docSurname) {
        this.con = con; this.docName = docName ; this.docSurname = docSurname;
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        window = primaryStage;
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        scene = new Scene(layout, 500, 500);
        window.setScene(scene);
        viewSchedule();
        window.show();

    }

    public void viewSchedule() throws SQLException {

        Statement stmt = null;
        String query =
                "select day, beginning, end  " +
                        "from " + "clinicdb" + ".office_hours" + " JOIN " + "clinicdb.doctors " + "ON" + "(clinicdb.office_hours.doctor = clinicdb.doctors.PWZ) " +
                        "WHERE " + "clinicdb.doctors.name = " + docName + " AND " + "clinicdb.doctors.surname = " + docSurname;

        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String begTime = rs.getString("beginning");
                String endTime = rs.getString("end");

                System.out.println(begTime + " " + endTime + "\t");
            }
        } catch (SQLException e ) {
            System.out.println("nie masz uprawnień!!!");

        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }

}
