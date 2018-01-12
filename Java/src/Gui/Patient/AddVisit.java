package Gui.Patient;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class AddVisit extends Application {
    public static Stage window = new Stage();
    private Scene scene;
    private Connection con;

    public static void main(String[] args) {
        launch(args);
    }

    public AddVisit(Connection con) {
        this.con = con;
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        scene = new Scene(layout, 500, 500);
        window.setScene(scene);

        window.show();

    }
}
