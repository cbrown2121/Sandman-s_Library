import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AddInventory extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Add Inventory");

        GridPane addInventory = new GridPane();
        addInventory.setAlignment(Pos.CENTER);
        addInventory.setHgap(5);
        addInventory.setVgap(5);
        addInventory.setPadding(new Insets(25, 25, 25, 25));

        Scene addScene = new Scene(addInventory, 500, 400);
        primaryStage.setScene(addScene);

        // Drop down menu for type of device
        Label typeLabel = new Label("Select device type");
        addInventory.add(typeLabel, 0, 0);
        ComboBox<String> deviceType = new ComboBox<>();
        deviceType.getItems().addAll("Desktop", "Laptop", "Tablet", "Monitor", "Input device");
        addInventory.add(deviceType, 1, 0);
        

        // Text fields for adding inventory
        // Name device
        Label nameDeviceLabel = new Label("Name of Device: ");
        addInventory.add(nameDeviceLabel, 0, 2);
        TextField nameDeviceField = new TextField();
        addInventory.add(nameDeviceField, 1, 2);

        // Serial number
        Label serialLabel = new Label("Serial Number: ");
        addInventory.add(serialLabel, 0, 3);
        TextField serialField = new TextField();
        addInventory.add(serialField, 1, 3);

        // Brand
        Label brandLabel = new Label("Brand: ");
        addInventory.add(brandLabel, 0, 4);
        TextField brandField = new TextField();
        addInventory.add(brandField, 1, 4);

        // Assigned User
        Label userLabel = new Label("User assigned: ");
        addInventory.add(userLabel, 0, 5);
        TextField userField = new TextField();
        addInventory.add(userField, 1, 5);

        // Location
        Label locationLabel = new Label("Location: ");
        addInventory.add(locationLabel, 0, 6);
        TextField locationField = new TextField();
        addInventory.add(locationField, 1, 6);

        // Buttons
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String deviceTypeValue = deviceType.getValue();
            String deviceName = nameDeviceField.getText();
            String serialNumber = serialField.getText();
            String deviceBrand = brandField.getText();
            String assignedUser = userField.getText();
            String deviceLocation = locationField.getText();
            String inventoryItem = "Serial Number: " +serialNumber + "\nBrand: " + deviceBrand + "\nAssigned user: " + assignedUser + "\nLocation: " + deviceLocation;

            // Add device to DataRepository
            DataRepository dataRepository = DataRepository.getInstance();
            dataRepository.addDevice(deviceTypeValue, deviceName, inventoryItem);

            // Add user to DataRepository
            dataRepository.addUser(assignedUser);

            // Assign device to user
            dataRepository.assignDeviceToUser(assignedUser, deviceName, inventoryItem);

            // Navigate back to the main menu
            MainMenu mainMenu = new MainMenu();
            mainMenu.start(primaryStage);
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            // Navigate back to the main menu
            MainMenu mainMenu = new MainMenu();
            mainMenu.start(primaryStage);
        });

        addInventory.add(submitButton, 5, 9);
        addInventory.add(cancelButton, 0, 9);

        primaryStage.show();
    }
}