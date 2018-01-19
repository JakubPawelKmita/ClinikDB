package Gui.Patient;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.text.View;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainPatient extends Application {
    public static Stage window = new Stage();
    private Scene scene;
    private Button addVisit, showDoctor;
    private TextField docName, docSurname;
    private Label docNameLabel, docSurnameLabel, label;
    private java.sql.Connection con;

    public MainPatient(java.sql.Connection con) {
        this.con = con;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        addVisit = new Button("Add Visit");
        showDoctor = new Button("Show Doctor's visit hours");
        addVisit.setOnAction(e -> setVisit());
        showDoctor.setOnAction(e -> {
            try {
                viewDoctor();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        docName = new TextField();
        docSurname = new TextField();

        docNameLabel = new Label("Enter doctor's name");
        docSurnameLabel = new Label("Enter doctor's surname");
        label = new Label("");


        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(addVisit, docNameLabel, docName, docSurnameLabel, docSurname, showDoctor);
        scene = new Scene(layout, 500, 500);
        window.setScene(scene);

        window.show();

    }

    private void viewDoctor() throws SQLException {
        PreparedStatement pstmt;
        pstmt = con.prepareStatement("SELECT PWZ FROM doctors WHERE name = ? AND surname = ?");
        pstmt.setString(1, docName.getText());
        pstmt.setString(2, docSurname.getText());
        pstmt.execute();
        ResultSet rs = pstmt.executeQuery();
        ViewDoctorSchedule view = new ViewDoctorSchedule(con, docName.getText(), docSurname.getText());
        pstmt.close();
    }

    private void setVisit() {
        AddVisit visit = new AddVisit(con);
        visit.start(AddVisit.window);
    }
}
