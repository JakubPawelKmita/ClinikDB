package Gui.Doctor;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainDoctor extends Application {
    public static Stage window = new Stage();
    private Scene scene;
    private java.sql.Connection con;

    public MainDoctor(java.sql.Connection con) {
        this.con = con;
    }

    public static void main(String[] args) {
        launch(args);
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
