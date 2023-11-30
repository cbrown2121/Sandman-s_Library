/*import java.util.ArrayList;
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

public class Tablets extends Application {
    private ListView<String> deviceListView;
    private TextArea deviceInfoTextArea;
    private int selectedDeviceIndex = -1;
    private DataRepository dataRepository = DataRepository.getInstance();
    

    public static void main(String [] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tablets");

        // Grab necessary information from the data repository
        DataRepository dataRepository = DataRepository.getInstance();
        String deviceType = "Tablet";
        ArrayList<String> deviceList = dataRepository.getDeviceByType(deviceType);
        
        // Setup grid pane
        GridPane tabletsGridPane = new GridPane();
        tabletsGridPane.setAlignment(Pos. CENTER);
        tabletsGridPane.setHgap(5);
        tabletsGridPane.setVgap(5);
        tabletsGridPane.setPadding(new Insets(25, 25, 25, 25));
 
        // List dekstops
        deviceListView = new ListView<>();
        deviceListView.getItems().addAll(deviceList);
        deviceListView.setOnMouseClicked(e -> {
            selectedDeviceIndex = deviceListView.getSelectionModel().getSelectedIndex();
            showDeviceInfo(deviceListView.getSelectionModel().getSelectedItem());
        });

        tabletsGridPane.add(deviceListView, 0, 0);

        deviceInfoTextArea = new TextArea();
        deviceInfoTextArea.setEditable(false);
        tabletsGridPane.add(deviceInfoTextArea, 1, 0);

        // Create the Save button during GUI initialization
        Button saveButton = new Button("Save");
        saveButton.setVisible(false);
        saveButton.setOnAction(saveEvent -> {
                if (selectedDeviceIndex >= 0) {
                    String updatedDetails = deviceInfoTextArea.getText();

                    if (dataRepository != null) {
                        System.out.println("Selected device index: " + selectedDeviceIndex);
                        System.out.println("Updated details: " + updatedDetails);

                        dataRepository.updateDeviceDetails(selectedDeviceIndex, updatedDetails);

                        System.out.println("Device details after update: " + dataRepository.getDeviceDetails());

                        ArrayList<String> updatedDeviceList = dataRepository.getDeviceByType("Tablet");
                        System.out.println("Updated device list: " + updatedDeviceList);

                        deviceListView.getItems().setAll(updatedDeviceList);
                        showDeviceInfo(deviceListView.getSelectionModel().getSelectedItem());

                        deviceInfoTextArea.setEditable(false);
                        saveButton.setVisible(false);
                        System.out.println("Save button clicked");
                    }
                   
                }     
        });

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            selectedDeviceIndex = deviceListView.getSelectionModel().getSelectedIndex();

            if (selectedDeviceIndex >= 0) {
                String selectedDeviceName = deviceListView.getSelectionModel().getSelectedItem();
                System.out.println("Selected Device Index: " + selectedDeviceIndex);
                System.out.println("Selected Device Name: " + selectedDeviceName);


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

        tabletsGridPane.add(backButton, 0, 10);
        tabletsGridPane.add(editButton, 5,10);
        tabletsGridPane.add(saveButton,5,11);
        tabletsGridPane.add(removeButton, 5,12);
        
        Scene tabletScene = new Scene(tabletsGridPane, 800, 600);
        primaryStage.setScene(tabletScene);
        primaryStage.show();
    }

    private void showDeviceInfo(String selectedTablet) {
        if (selectedTablet != null) {
            String deviceInfo = getDeviceInfo(selectedTablet);
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
    
        for (int i = 0; i < deviceNames.size(); i++) {
            if (deviceNames.get(i) != null && deviceNames.get(i).startsWith(deviceName)) {
                if (i < deviceDetails.size()) {
                    return deviceDetails.get(i);
                }
            }
        }
    
        return "device not found";
    }

    /*private void handleRemove() {
        int selectedIndex = deviceListView.getSelectionModel().getSelectedIndex();
        System.out.println("Selected index: " + selectedIndex);
        if (selectedIndex >= 0) {
            selectedDeviceIndex = selectedIndex;

            boolean confirmed = removeConfirmation();
            if (confirmed) {

                dataRepository.removeDevice(selectedIndex);
                ArrayList<String> updatedDeviceList = dataRepository.getDeviceByType("Desktop");
                deviceListView.getItems().setAll(updatedDeviceList);

                deviceInfoTextArea.clear();
            }
        }
    }

    private boolean removeConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Remove Device");
        alert.setContentText("Are you sure you want to remove this device?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void handleRemove() {
        int selectedIndex = deviceListView.getSelectionModel().getSelectedIndex();
        System.out.println("Selected index in handleRemove (Tablet): " + selectedIndex);
    
        String deviceType = "Tablet";
        System.out.println("Selected device type: " + deviceType);
    
        boolean confirmed = removeConfirmation(selectedIndex, deviceType);
        if (confirmed) {
            System.out.println("Executing removeDevice for index: " + selectedIndex);
            dataRepository.removeDevice(selectedIndex);
            ArrayList<String> updatedDeviceList = dataRepository.getDeviceByType("Tablet");
            deviceListView.getItems().setAll(updatedDeviceList);
            deviceInfoTextArea.clear();
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
    
    
}*/

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

public class Tablets extends Application {
    private ListView<String> deviceListView;
    private TextArea deviceInfoTextArea;
    private int selectedDeviceIndex = -1;
    private DataRepository dataRepository = DataRepository.getInstance();

    public static void main(String [] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tablets");

        DataRepository dataRepository = DataRepository.getInstance();
        dataRepository.getDeviceByType("Tablet");

        // Setup grid pane
        GridPane tabletsGridPane = new GridPane();
        tabletsGridPane.setAlignment(Pos. CENTER);
        tabletsGridPane.setHgap(5);
        tabletsGridPane.setVgap(5);
        tabletsGridPane.setPadding(new Insets(25, 25, 25, 25));
 
        // List tablets
        deviceListView = new ListView<>();
        deviceListView.getItems().addAll(dataRepository.getDeviceByType("Tablet"));
        deviceListView.setOnMouseClicked(e -> {
            selectedDeviceIndex = deviceListView.getSelectionModel().getSelectedIndex();
            showDeviceInfo(deviceListView.getSelectionModel().getSelectedItem());
        });

        tabletsGridPane.add(deviceListView, 0, 0);

        deviceInfoTextArea = new TextArea();
        deviceInfoTextArea.setEditable(false);
        tabletsGridPane.add(deviceInfoTextArea, 1, 0);

        // Create the Save button during GUI initialization
        Button saveButton = new Button("Save");
        saveButton.setVisible(false);
        saveButton.setOnAction(saveEvent -> {
                if (selectedDeviceIndex >= 0) {
                    String updatedDetails = deviceInfoTextArea.getText();

                    if (dataRepository != null) {

                        dataRepository.updateDeviceDetails(selectedDeviceIndex, updatedDetails);

                        ArrayList<String> updatedDeviceList = dataRepository.getDeviceByType("Tablet");
                        
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

        tabletsGridPane.add(backButton, 0, 10);
        tabletsGridPane.add(editButton, 5,10);
        tabletsGridPane.add(saveButton,5,11);
        tabletsGridPane.add(removeButton, 5,12);
        
        Scene tabletScene = new Scene(tabletsGridPane, 800, 600);
        primaryStage.setScene(tabletScene);
        primaryStage.show();
    }

    private void showDeviceInfo(String selectedTablet) {
        if (selectedTablet != null) {
            String deviceInfo = getDeviceInfo(selectedTablet);
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

            if (removeConfirmation(selectedIndex, "Tablet")) {
                dataRepository.removeDevice(selectedIndex);
                ArrayList<String> updatedDeviceList = dataRepository.getDeviceByType("Tablet");
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






