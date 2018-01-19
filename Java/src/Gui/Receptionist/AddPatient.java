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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddPatient extends Application {
    private Connection con;
    private Scene scene;
    public static Stage window = new Stage();
    private Label pesel, name, surname, birthday, city, street, h_number, f_number, post_code, post_office, phone, label;
    private TextField peselt, namet, surnamet, birthdayt, cityt, streett, h_numbert, f_numbert, post_codet, post_officet, phonet;
    private Button add;

    public static void main(String[] args) {
        launch(args);
    }

    public AddPatient(Connection con) {
        this.con = con;
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        pesel = new Label("Enter pesel");
        name = new Label("Enter name");
        surname = new Label("Enter surname");
        birthday = new Label("Enter birthday date");
        city = new Label("Enter city");
        street = new Label("Enter street");
        h_number = new Label("Enter house's number");
        f_number = new Label("Enter flat's number (optional)");
        post_code = new Label("Enter postal code");
        post_office = new Label("Enter postal office");
        phone = new Label("Enter phone number");
        label = new Label("");

        peselt = new TextField();
        namet = new TextField();
        surnamet = new TextField();
        birthdayt = new TextField();
        cityt = new TextField();
        streett = new TextField();
        h_numbert = new TextField();
        f_numbert = new TextField();
        post_codet = new TextField();
        post_officet = new TextField();
        phonet = new TextField();

        add = new Button("Add new patient");
        add.setOnAction(e -> addPatient());

        VBox layout = new VBox(5);
        layout.setPadding(new Insets(10, 20, 20, 20));
        layout.getChildren().addAll(pesel, peselt, name, namet, surname, surnamet, birthday, birthdayt, city, cityt, street, streett, h_number, h_numbert, f_number, f_numbert, post_code, post_codet, post_office, post_officet, phone, phonet, label, add);

        scene = new Scene(layout, 500, 680);
        window.setScene(scene);

        window.show();


    }

    private void addPatient() {

        PreparedStatement pstmt;
        try {
            pstmt = con.prepareStatement("INSERT INTO patients VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)");
            pstmt.setString(1, peselt.getText());
            pstmt.setString(2, namet.getText());
            pstmt.setString(3, surnamet.getText());
            pstmt.setString(4, birthdayt.getText());
            pstmt.setString(5, cityt.getText());
            pstmt.setString(6, streett.getText());
            pstmt.setString(7, h_numbert.getText());
            pstmt.setString(8, f_numbert.getText());
            pstmt.setString(9, post_codet.getText());
            pstmt.setString(10, post_officet.getText());
            pstmt.setString(11, phonet.getText());
            pstmt.execute();
            pstmt.close();
            System.out.println("Udało się dodać pacjenta");
        } catch (SQLException e) {
            label.setText("Wprowadziłeś błędne dane");

        }
    }
}
