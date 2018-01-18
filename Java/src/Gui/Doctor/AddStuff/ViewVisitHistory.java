package Gui.Doctor.AddStuff;

import Gui.Receptionist.PatientInfo;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewVisitHistory extends Application {
    public static Stage window = new Stage();
    private Scene scene;
    private Connection con;
    private String id;
    private TableView<VisitHistoryInfo> table;
    private TableColumn<VisitHistoryInfo, String> date = new TableColumn<>("Date");
    private TableColumn<VisitHistoryInfo, String> disease = new TableColumn<>("Disease");
    private TableColumn<VisitHistoryInfo, String> advice = new TableColumn<>("Advice");
    private TableColumn<VisitHistoryInfo, String> medicine = new TableColumn<>("Medicine");
    private VisitHistoryInfo vh;

    public ViewVisitHistory(Connection con, String id) {
        this.con = con;
        this.id = id;
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        date.setMinWidth(50);
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        disease.setMinWidth(50);
        disease.setCellValueFactory(new PropertyValueFactory<>("disease"));

        advice.setMinWidth(50);
        advice.setCellValueFactory(new PropertyValueFactory<>("advice"));

        medicine.setMinWidth(50);
        medicine.setCellValueFactory(new PropertyValueFactory<>("medicine"));

        table = new TableView<>();
        table.getColumns().addAll(date, disease, advice, medicine);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().add(table);
        scene = new Scene(layout, 500, 500);
        window.setScene(scene);
        showVisitHistory();
        window.show();

    }

    private void showVisitHistory() {
        PreparedStatement pstmt;
        try {
            pstmt = con.prepareStatement("SELECT\n" +
                    "  v.date        AS DATE,\n" +
                    "  d.name        AS Disease,\n" +
                    "  `v h`.advices AS ADVICES,\n" +
                    "  m.name        AS Medicine,\n" +
                    "  doc.surname   AS DOCTOR\n" +
                    "FROM ((((visits v RIGHT JOIN `visit_history` `v h` ON v.ID = `v h`.visit_ID) LEFT JOIN (recognition r\n" +
                    "  JOIN diseases d ON r.disease = d.ID) ON `v h`.ID = r.visit_ID) LEFT JOIN (prescription p\n" +
                    "  JOIN medicines m ON p.medicine = m.ID) ON `v h`.ID = p.visit_ID) JOIN doctors doc ON v.Doctor = doc.PWZ) JOIN\n" +
                    "  patients pat ON pat.PESEL = v.Patient\n" +
                    "WHERE v.Patient = ? OR (pat.name = 'Janusz' AND pat.surname = 'Krowin-Mekka')\n" +
                    "ORDER BY DATE;");
            pstmt.setString(1, id);
            pstmt.execute();
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                String d,n,a,m;
                d = rs.getString("DATE");
                n = rs.getString("Disease");
                a = rs.getString("ADVICES");
                m = rs.getString("Medicine");
                vh = new VisitHistoryInfo(d, n, a, m);
                table.getItems().add(vh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
