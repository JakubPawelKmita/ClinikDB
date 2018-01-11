package Gui;

import Core.Connection;
import Core.UserType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private Stage window;
    private Label login, pwd, user;
    private Button connect;
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

        pwd = new Label("Type in password");
        tPwd = new TextField();

        user = new Label("Set type");

        for (String s : new UserType().getUsers()) {
            userType.getItems().addAll(s);
        }

        bSet = new Button("Set");
        bSet.setOnAction(e -> setType());

        connect = new Button("Log in");
        connect.setOnAction(e -> getConnection());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(login, tLogin, pwd, tPwd, user, userType, bSet, connect);

        scene = new Scene(layout, 500, 500);
        window.setScene(scene);

        window.show();
    }

    private void setType() {
        actualUser = userType.getSelectionModel().getSelectedItem();
    }

    private void getConnection() {
        java.sql.Connection connection = new Connection(actualUser).connect();
    }
}
