package Gui.Receptionist;

import com.mysql.jdbc.MysqlDataTruncation;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.sound.midi.SysexMessage;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetHours extends Application {
    public static Stage window = new Stage();
    private Connection con;
    private Scene scene;
    private Button add;
    private Label name, surname, hoursS, hoursE, day;
    private TextField namet, surnamet, hoursST, hoursSE, dayt;
    private Label label = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    public SetHours(Connection con) {
        this.con = con;
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        name = new Label("Enter doctor's PWZ");
        hoursS = new Label("Enter beggining hour");
        hoursE = new Label("Enter ending hour");
        day = new Label("Enter day");
        namet = new TextField();
        hoursST = new TextField();
        hoursSE = new TextField();
        dayt = new TextField();
        add = new Button("Add");
        add.setOnAction(e -> add());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(name, namet, hoursS, hoursST, hoursE, hoursSE, day, dayt, add, label);
        scene = new Scene(layout, 500, 500);
        window.setScene(scene);
        window.show();
    }

    private void add() {
        PreparedStatement pstmt;
        try {
            pstmt = con.prepareStatement("UPDATE office_hours SET doctor = ?, day = ?, beginning = ?, end = ? WHERE doctor = ? AND day = ?");
            pstmt.setString(1, namet.getText());
            pstmt.setString(2, dayt.getText());
            pstmt.setString(3, hoursST.getText());
            pstmt.setString(4, hoursSE.getText());
            pstmt.setString(5, namet.getText());
            pstmt.setString(6, dayt.getText());
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("DODANO GODZINY URZEDOWANIA");
        } catch (SQLException e) {
           label.setText("Niepoprawny typ danych");
        }

    }
}
