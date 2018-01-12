package Gui.Doctor.History;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddMedicineOnHistory extends Application {
    public static Stage window = new Stage();
    private Scene scene;

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
