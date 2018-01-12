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
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UpdateVisit extends Application {
    public static Stage window = new Stage();
    private Scene scene;
    private Connection con;
    private Button show, update;
    private int visitID = 5;

    public static void main(String[] args) {
        launch(args);
    }

    public UpdateVisit(Connection con) {
        this.con = con;
    }
    //TODO PRZENIESIENIE WIZYT DO GUI I PODPIECIE ICH
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        show = new Button("Show visits");
        show.setOnAction(e -> showVisits());
        update = new Button("Update visit");
        update.setOnAction(e -> updateVisit());
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(show, update);
        scene = new Scene(layout, 500, 500);
        window.setScene(scene);

        window.show();

    }
   private void showVisits() {
       // SELECT * FROM VISITS
       try {
           PreparedStatement pstmt = con.prepareStatement("SELECT * FROM visits");
           pstmt.execute();
           ResultSet rs = pstmt.executeQuery();
           while (rs.next()) {
               String patient = rs.getString("Patient");
               String conf = rs.getString("confirmation");
               String date = rs.getString("date");
               String time = rs.getString("time");

               System.out.println(patient + " " + date + " " + time + " " + conf + "\t");
           }
           System.out.println("wyświetlono wizyty dla recepcjonisty");
       } catch (SQLException e) {e.printStackTrace();}
   }

   private void updateVisit() {
       //UPDATE `visits` SET `confirmation` = '1' WHERE `visits`.`ID` = 10;
       try {
            PreparedStatement pstmt = con.prepareStatement("UPDATE visits SET confirmation = 1 WHERE visits.ID = " + visitID);
            pstmt.execute();
            System.out.println("Wizyta updejtowana");
       } catch (SQLException e) {e.printStackTrace();}
   }

   private void setVisitID() {

   }

}
