import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Main Menu");

        //Label and buttons
        Button addButton = new Button("Add inventory");
        Button viewButton = new Button("View inventory");
        Button userButton = new Button("View users");
        Button exitButton = new Button("Exit");
        Label mainMenuLabel = new Label("Welcome to Sandman's Library!");

        // Determine what each button does
        addButton.setOnAction(e -> openAddInventory(primaryStage));
        viewButton.setOnAction(e -> openViewInventory(primaryStage));
        userButton.setOnAction(e -> openViewUsers(primaryStage));
        exitButton.setOnAction(e -> Platform.exit());

        // Set Layout
        VBox mainMenuLayout = new VBox(10);
        mainMenuLayout.getChildren().addAll(mainMenuLabel, addButton, viewButton, userButton, exitButton);
        Scene mainMenuScene = new Scene(mainMenuLayout, 400, 300);
        mainMenuLayout.setAlignment(Pos.CENTER);

        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    private void openAddInventory(Stage primaryStage) {
        AddInventory addInventory = new AddInventory();
        addInventory.start(primaryStage);
    }

    private void openViewInventory(Stage primaryStage) {
        ViewInventory viewInventory = new ViewInventory();
        try {
            viewInventory.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openViewUsers(Stage primaryStage) {
        ViewUsers viewUsers = new ViewUsers();
        viewUsers.start(primaryStage);
    }
}



