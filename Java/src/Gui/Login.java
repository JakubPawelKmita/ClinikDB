package Gui;

import Core.ConnectionClass;
import Core.UserType;
import Gui.Doctor.MainDoctor;
import Gui.Patient.MainPatient;
import Gui.Receptionist.MainReceptionist;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.SQLException;

public class Login extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private static Stage window;
    private Label login, pwd, user, backup;
    private Button connect, make, load;
    private TextField tLogin, tPwd;
    private ComboBox<String> userType = new ComboBox<String>();
    private String actualUser;
    private Button bSet;
    private Scene scene;
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        login = new Label("Type in login");
        tLogin = new TextField();
        backup = new Label("");

        pwd = new Label("Type in password");
        tPwd = new TextField();

        user = new Label("Set type");

        for (String s : new UserType().getUsers()) {
            userType.getItems().addAll(s);
        }

        bSet = new Button("Set");
        bSet.setOnAction(e -> setType());
        make = new Button("Make backup");
        make.setOnAction(e -> makeBackup());
        load = new Button("Load backup");
        load.setOnAction(e -> loadBackup());


        connect = new Button("Log in");
        connect.setOnAction(e -> {
            try {
                getConnection();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(user, userType, bSet, connect, make, load, backup);

        scene = new Scene(layout, 500, 500);
        window.setScene(scene);

        window.show();
    }

    private void loadBackup() {
        String path, path2;
        path = "C:\\Users\\pawit\\Desktop\\backup\\backup";
        Process exec;
        String dbpass;
        String cmd = "cmd.exe";
        String c = "/c";
        FileChooser fc = new FileChooser();
        try {
            FileInputStream fstream = new FileInputStream("root.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            dbpass = br.readLine();
            br.close();
            fstream.close();
        } catch ( IOException e1) {
            e1.printStackTrace();
            return;
        }
        path = fc.showOpenDialog(load.getScene().getWindow()).toString();
        String[] execCmd = new String[]{cmd, c, "mysql -u root -p" + dbpass + " clinicdb < C:\\Users\\pawit\\Desktop\\backup\\backup.sql"};
        try {
            exec = Runtime.getRuntime().exec(execCmd);
            int proc = exec.waitFor();
            if (proc == 0) {
                backup.setText("Database successfully restored");
            } else backup.setText("Could not restore database");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void makeBackup() {
        String path, path2, dbpass;
        try {
            FileInputStream fstream = new FileInputStream("root.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            dbpass = br.readLine();
            br.close();
            fstream.close();
        } catch ( IOException e1) {
            e1.printStackTrace();
            return;
        }
        path = "C:\\Users\\pawit\\Desktop\\backup\\backup";
        String test = "C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump -u root -p"+ dbpass + " --add-drop-database -B clinicdb -r " + path + "backup";
        Process exec;
        Process runtimeProcess;
        String cmd = "cmd.exe";
        String c = "/c";
        String DBPASS;

        String command2 = "mysqldump -u root -p" + dbpass+ " clinicdb -R -E --triggers > C:\\Users\\pawit\\Desktop\\backup\\backup.sql";
        try {

            runtimeProcess = Runtime.getRuntime().exec(new String[]{cmd,c,command2});
            DBPASS = "";
            command2 = "";
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                System.out.println("DZIALA BACKUP");
                backup.setText("Backup successfully made!");
                InputStream inputStream = runtimeProcess.getInputStream();
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                String str = new String(buffer);
                System.out.println(str);

            } else {
                System.out.println("Error while making backup!");
                InputStream errorStream = runtimeProcess.getErrorStream();
                byte[] buffer = new byte[errorStream.available()];
                errorStream.read(buffer);

                String str = new String(buffer);
                System.out.println(str);


            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    private void setType() {
        actualUser = userType.getSelectionModel().getSelectedItem();
    }

    private void getConnection() throws SQLException {

        ConnectionClass connection = new ConnectionClass(actualUser);
        connection.connect();
        connection.viewTable(connection.getConnectionRef(), "clinicdb");
        if (actualUser == "lekarz") {
            MainDoctor doctor = new MainDoctor(connection.getConnectionRef());
            doctor.start(MainDoctor.window);
        }
        else if (actualUser == "pacjent") {
            MainPatient patient = new MainPatient(connection.getConnectionRef(), connection.getActualUser());
            patient.start(MainPatient.window);
        }
        else if (actualUser == "recepcjonista") {
            MainReceptionist receptionist = new MainReceptionist(connection.getConnectionRef());
            receptionist.start(MainReceptionist.window);
        }
        window.close();
    }
}
