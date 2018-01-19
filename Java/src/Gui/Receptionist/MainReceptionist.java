package Gui.Receptionist;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainReceptionist extends Application {
    public static Stage window = new Stage();
    private Scene scene;
    private java.sql.Connection con;
    private Button delete, update, show, addDoc, set, addPat;
    private String visitID;
    private TableView<PatientInfo> table;
    private TableColumn<PatientInfo, String> id = new TableColumn<PatientInfo, String>("ID");
    private TableColumn<PatientInfo, String> pesel = new TableColumn<PatientInfo, String>("Pesel");
    private TableColumn<PatientInfo, String> date = new TableColumn<PatientInfo, String>("Date");
    private TableColumn<PatientInfo, String> hour = new TableColumn<PatientInfo, String>("Hour");
    private TableColumn<PatientInfo, String> conf = new TableColumn<PatientInfo, String>("Confirmation");
    private PatientInfo patientInfo;
    private PatientInfo row;
    private Label label;

    public MainReceptionist(java.sql.Connection con) {
            this.con = con;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        id.setMinWidth(50);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        pesel.setMinWidth(50);
        pesel.setCellValueFactory(new PropertyValueFactory<>("pesel"));

        date.setMinWidth(50);
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        hour.setMinWidth(50);
        hour.setCellValueFactory(new PropertyValueFactory<>("hour"));

        conf.setMinWidth(20);
        conf.setCellValueFactory(new PropertyValueFactory<>("confirmation"));

        table = new TableView<>();
        table.getColumns().addAll(pesel, date, hour, conf);
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                row = table.getSelectionModel().getSelectedItem();
            }
        });
        label = new Label("");
        show = new Button("Show visits");
        show.setOnAction(e -> showVisits());
        delete = new Button("Delete visit");
        delete.setOnAction(e -> deleteVisit());
        update = new Button("Update visit");
        update.setOnAction(e -> updateVisit());
        delete = new Button("Delete visit");
        delete.setOnAction(e -> deleteVisit());
        addDoc = new Button("Add Doctor");
        addDoc.setOnAction(e-> addDoctor());
        set = new Button("Set hours for a doctor");
        set.setOnAction(e -> setHours());
        addPat = new Button("Add new patient");
        addPat.setOnAction(e -> addNewPatient());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(show, update, delete, addDoc, addPat, set, label, table);
        scene = new Scene(layout, 500, 500);
        window.setScene(scene);

        window.show();

    }

    private void addNewPatient() {
        AddPatient addPatient = new AddPatient(con);
        addPatient.start(AddPatient.window);
    }

    private void setHours() {
        SetHours setHours = new SetHours(con);
        setHours.start(SetHours.window);
    }

    private void addDoctor() {
        AddDoctor addDoctor = new AddDoctor(con);
        addDoctor.start(AddDoctor.window);
    }

    private void deleteVisit() {
        try {
            visitID = row.getId();
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM visits WHERE visits.ID = ?");
            pstmt.setString(1, visitID);
            pstmt.execute();
            showVisits();
            label.setText("Usunięto wizytę");
        } catch (SQLException e) {
            label.setText("Wystąpił błąd podczas wykonywania operacji!");
        } catch (NullPointerException e) {
            label.setText("Nie wybrałeś wizyty do usunięcia!");
        }
    }

    private void showVisits() {
        // SELECT * FROM VISITS
        try {
            table.getItems().clear();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM visits");
            pstmt.execute();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("ID");
                String patient = rs.getString("Patient");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String conf = rs.getString("confirmation");
                patientInfo = new PatientInfo(id, patient, date, time, conf);
                table.getItems().add(patientInfo);
                System.out.println(patient + " " + date + " " + time + " " + conf + "\t");
            }
            System.out.println("wyświetlono wizyty dla recepcjonisty");
        } catch (SQLException e) {label.setText("Wystąpił błąd podczas wykonywania operacji!");}
    }

    private void updateVisit() {
        //UPDATE `visits` SET `confirmation` = '1' WHERE `visits`.`ID` = 10;
        try {
            visitID = row.getId();
            PreparedStatement pstmt = con.prepareStatement("UPDATE visits SET confirmation = 1 WHERE visits.ID = ?");
            pstmt.setString(1, visitID);
            pstmt.execute();
            showVisits();
            System.out.println("Wizyta updejtowana");
        } catch (SQLException e) {label.setText("Wprowadziłeś błędne dane");}
          catch (NullPointerException e) {
                label.setText("Nie wybrałeś wizyty do updatetowania");
          }
    }

}
