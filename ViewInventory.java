import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewInventory extends Application{
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("View Inventory");

        // Buttons
        Button desktopButton = new Button("View desktops");
        Button laptopButton = new Button("View laptops");
        Button tabletButton = new Button("View tablets");
        Button monitorButton = new Button("View monitors");
        Button inputButton = new Button("View input devices");
        Button backButton = new Button("Return to main menu");

        // Determine what each button does
        desktopButton.setOnAction(e -> openDesktops(primaryStage));
        laptopButton.setOnAction(e -> openLaptops(primaryStage));
        tabletButton.setOnAction(e -> openTablets(primaryStage));
        monitorButton.setOnAction(e -> openMonitors(primaryStage));
        inputButton.setOnAction(e -> openInputDevices(primaryStage));
        backButton.setOnAction(e -> openMainMenu(primaryStage));


        VBox viewInventoryLayout = new VBox(10);
        viewInventoryLayout.getChildren().addAll(desktopButton, laptopButton, tabletButton, monitorButton, inputButton, backButton);
        Scene viewInventoryScene = new Scene(viewInventoryLayout, 400, 300);
        viewInventoryLayout.setAlignment(Pos.CENTER);

        primaryStage.setScene(viewInventoryScene);
        primaryStage.show();
    
    }

    private void openDesktops(Stage primaryStage) {
            Desktops viewInventory = new Desktops();
            viewInventory.start(primaryStage);
    }

    private void openMainMenu(Stage primaryStage) {
        MainMenu viewInventory = new MainMenu();
        viewInventory.start(primaryStage);
    }

    private void openLaptops(Stage primaryStage) {
        Laptops viewInventory = new Laptops();
        viewInventory.start(primaryStage);
    }

    private void openTablets(Stage primaryStage) {
        Tablets viewInventory = new Tablets();
        viewInventory.start(primaryStage);
    }

    private void openMonitors(Stage primaryStage) {
        Monitors viewInventory = new Monitors();
        viewInventory.start(primaryStage);
    }

    private void openInputDevices(Stage primaryStage) {
        InputDevices viewInventory = new InputDevices();
        viewInventory.start(primaryStage);
    }
}