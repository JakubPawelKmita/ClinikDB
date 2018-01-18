package Gui.Receptionist;

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

public class AddDoctor extends Application {
    private Connection con;
    private Scene scene;
    public static Stage window = new Stage();
    private Label name, pwz, surname, phone;
    private TextField namet, pwzt, surnamet, phonet;
    private Button addDoc;


    public static void main(String[] args) {
        launch(args);
    }

    public AddDoctor(Connection con) {
        this.con = con;
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        name = new Label("Enter name");
        surname = new Label("Enter surname");
        pwz = new Label("Enter PWZ");
        phone = new Label("Enter phone number");
        namet = new TextField();
        pwzt = new TextField();
        surnamet = new TextField();
        phonet = new TextField();
        addDoc = new Button("Add");
        addDoc.setOnAction(e -> addDoctor());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(name, namet, surname, surnamet, pwz, pwzt, phone, phonet, addDoc);
        scene = new Scene(layout, 500, 500);
        window.setScene(scene);
        window.show();

    }

    private void addDoctor() {
        PreparedStatement pstmt;
        try {
            pstmt = con.prepareStatement("INSERT INTO doctors VALUES (?, ?, ?, ?)");
            pstmt.setString(1, pwzt.getText());
            pstmt.setString(2, namet.getText());
            pstmt.setString(3, surnamet.getText());
            pstmt.setString(4, phonet.getText());
            pstmt.execute();
            System.out.println("DODANO LEKARZA");
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
