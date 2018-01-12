package Gui.Receptionist;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteVisit extends Application {
    public static Stage window = new Stage();
    private Scene scene;
    private Connection con;
    private Button show, delete;
    private int visitID = 2;
    public static void main(String[] args) {
        launch(args);
    }

    public DeleteVisit(Connection con) {
        this.con = con;
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        show = new Button("Show visits");
        show.setOnAction(e -> showVisits());
        delete = new Button("Delete visit");
        delete.setOnAction(e -> deleteVisit());
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(show, delete);
        scene = new Scene(layout, 500, 500);
        window.setScene(scene);

        window.show();
    }

    private void deleteVisit() {
        try {
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM visits WHERE visits.ID = " + visitID);
            pstmt.execute();
            System.out.println("Usunięto wizytę");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showVisits() {
        // SELECT * FROM VISITS
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM visits");
            pstmt.execute();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String patient = rs.getString("Patient");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String conf = rs.getString("confirmation");
                System.out.println(patient + " " + date + " " + time + " " + conf + "\t");
            }
            System.out.println("wyświetlono wizyty dla recepcjonisty");
        } catch (SQLException e) {e.printStackTrace();}
    }

    private void setVisitID() {

    }
}
