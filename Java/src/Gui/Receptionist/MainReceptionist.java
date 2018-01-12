package Gui.Receptionist;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainReceptionist extends Application {
    public static Stage window = new Stage();
    private Scene scene;
    private java.sql.Connection con;
    private Button delete, update;

    public MainReceptionist(java.sql.Connection con) {
            this.con = con;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        update = new Button("Update visit");
        update.setOnAction(e -> updateVisit());
        delete = new Button("Delete visit");
        delete.setOnAction(e -> deleteVisit());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(update, delete);
        scene = new Scene(layout, 500, 500);
        window.setScene(scene);

        window.show();

    }

    private void deleteVisit() {
        DeleteVisit deleteVisit = new DeleteVisit(con);
        deleteVisit.start(DeleteVisit.window);
    }

    private void updateVisit() {
        UpdateVisit updateVisit = new UpdateVisit(con);
        updateVisit.start(UpdateVisit.window);
    }

}
