import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;

public class InputDevices extends Application {
    private ListView<String> deviceListView;
    private TextArea deviceInfoTextArea;
    private int selectedDeviceIndex = -1;
    private DataRepository dataRepository = DataRepository.getInstance();

    public static void main(String [] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Input devices");

        DataRepository dataRepository = DataRepository.getInstance();
        dataRepository.getDeviceByType("Input device");

        // Setup grid pane
        GridPane inputdevicesGridPane = new GridPane();
        inputdevicesGridPane.setAlignment(Pos. CENTER);
        inputdevicesGridPane.setHgap(5);
        inputdevicesGridPane.setVgap(5);
        inputdevicesGridPane.setPadding(new Insets(25, 25, 25, 25));
 
        // List dekstops
        deviceListView = new ListView<>();
        deviceListView.getItems().addAll(dataRepository.getDeviceByType("Input device"));
        deviceListView.setOnMouseClicked(e -> {
            selectedDeviceIndex = deviceListView.getSelectionModel().getSelectedIndex();
            showDeviceInfo(deviceListView.getSelectionModel().getSelectedItem());
        });

        inputdevicesGridPane.add(deviceListView, 0, 0);

        deviceInfoTextArea = new TextArea();
        deviceInfoTextArea.setEditable(false);
        inputdevicesGridPane.add(deviceInfoTextArea, 1, 0);

        // Create the Save button during GUI initialization
        Button saveButton = new Button("Save");
        saveButton.setVisible(false);
        saveButton.setOnAction(saveEvent -> {
                if (selectedDeviceIndex >= 0) {
                    String updatedDetails = deviceInfoTextArea.getText();

                    if (dataRepository != null) {

                        dataRepository.updateDeviceDetails(selectedDeviceIndex, updatedDetails);

                        ArrayList<String> updatedDeviceList = dataRepository.getDeviceByType("Input device");
                        
                        deviceListView.getItems().setAll(updatedDeviceList);

                        deviceInfoTextArea.setEditable(false);
                        saveButton.setVisible(false);
                        selectedDeviceIndex = -1;

                        dataRepository.saveDetailsToFile();
                        
                    }
                   
                }     
        });

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            selectedDeviceIndex = deviceListView.getSelectionModel().getSelectedIndex();

            if (selectedDeviceIndex >= 0) {
                
                deviceInfoTextArea.setEditable(true);
                saveButton.setVisible(true);
            }
        });

        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> handleRemove());

        // back button
        Button backButton = new Button("Return to menu");
        backButton.setOnAction(e -> {
            try {
                ViewInventory viewInventory = new ViewInventory();
                viewInventory.start(primaryStage);
                } catch (Exception ex) {    
                    ex.printStackTrace();
                }
                primaryStage.show();
        });

        inputdevicesGridPane.add(backButton, 0, 10);
        inputdevicesGridPane.add(editButton, 5,10);
        inputdevicesGridPane.add(saveButton,5,11);
        inputdevicesGridPane.add(removeButton, 5,12);
        
        Scene inputdeviceScene = new Scene(inputdevicesGridPane, 800, 600);
        primaryStage.setScene(inputdeviceScene);
        primaryStage.show();
    }

    private void showDeviceInfo(String selectedInputdevice) {
        if (selectedInputdevice != null) {
            String deviceInfo = getDeviceInfo(selectedInputdevice);
            deviceInfoTextArea.setText(deviceInfo);
        } else {
            deviceInfoTextArea.setText("No device selected");
        }
    }
    
    private String getDeviceInfo(String deviceName) {
        if (deviceName == null) {
            return "device name is null";
        }
    
        DataRepository dataRepository = DataRepository.getInstance();
        ArrayList<String> deviceNames = dataRepository.getDeviceNames();
        ArrayList<String> deviceDetails = dataRepository.getDeviceDetails();
    
        // Iterate through deviceNames to find the index of the selected device
        for (int i = 0; i < deviceNames.size(); i++) {
            
            if (deviceName.equals(deviceNames.get(i))) {
                if (i < deviceDetails.size()) {
                    // Return the details directly based on the index
                    return dataRepository.getDeviceDetails().get(i);
                }
            }
        }
    
        return "device not found";
    }
    
    private void handleRemove() {
        int selectedIndex = deviceListView.getSelectionModel().getSelectedIndex();
        System.out.println("Selected index: " + selectedIndex);
        if (selectedIndex >= 0) {
            selectedDeviceIndex = selectedIndex;

            if (removeConfirmation(selectedIndex, "Input device")) {
                dataRepository.removeDevice(selectedIndex);
                ArrayList<String> updatedDeviceList = dataRepository.getDeviceByType("Input device");
                deviceListView.getItems().setAll(updatedDeviceList);

                deviceInfoTextArea.clear();
            }     
        }
    }

     private boolean removeConfirmation(int selectedIndex, String deviceType) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Remove Device");
        alert.setContentText("Are you sure you want to remove this " + deviceType + "?");
    
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}