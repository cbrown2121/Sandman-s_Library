import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Optional;

public class ViewUsers extends Application {
    private ListView<UserInfo> userList;
    private TextArea deviceInfoTextArea;
    private int selectedUserIndex = -1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("View Users");

        DataRepository dataRepository = DataRepository.getInstance();
        ArrayList<UserInfo> users = dataRepository.getUsers();

        dataRepository.loadUsersFromFile();

        

        // Setup grid pane
        GridPane viewUsersGridPane = new GridPane();
        viewUsersGridPane.setAlignment(Pos.CENTER);
        viewUsersGridPane.setHgap(5);
        viewUsersGridPane.setVgap(5);

        // List users
        userList = new ListView<>();
        userList.getItems().addAll(users);
        userList.setOnMouseClicked(e -> {
            selectedUserIndex = userList.getSelectionModel().getSelectedIndex();
            showAssignedDevices(userList.getSelectionModel().getSelectedItem());
        });

        viewUsersGridPane.add(userList, 1, 1);

        deviceInfoTextArea = new TextArea();
        deviceInfoTextArea.setEditable(false);
        viewUsersGridPane.add(deviceInfoTextArea, 2, 1);

        // Back button
        Button backButton = new Button("Return to menu");
        backButton.setOnAction(e -> {
            try {
                MainMenu mainMenu = new MainMenu();
                mainMenu.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            primaryStage.show();
        });

        // Save button
        Button saveButton = new Button("Save");
        saveButton.setVisible(false);
        saveButton.setOnAction(e -> {
            if (deviceInfoTextArea.isEditable() && selectedUserIndex >= 0) {
                    DataRepository repository = DataRepository.getInstance();

                    userList.getItems().setAll(repository.getUsers());
                    deviceInfoTextArea.clear();

                    selectedUserIndex = -1;

                    deviceInfoTextArea.setEditable(false);

                    dataRepository.saveUsersToFile();
                    dataRepository.saveUserAssignmentsToFile();   
            }
        });

        Button removeButton = new Button("Delete");
        removeButton.setOnAction(e -> handleRemove());

        // Edit button
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            deviceInfoTextArea.setEditable(true);
            saveButton.setVisible(true);
        });

        viewUsersGridPane.add(backButton, 0, 10);
        viewUsersGridPane.add(editButton,5,10);
        viewUsersGridPane.add(removeButton, 5,11);
        viewUsersGridPane.add(saveButton,5,12);

        Scene viewUsersScene = new Scene(viewUsersGridPane, 800, 600);
        primaryStage.setScene(viewUsersScene);
        primaryStage.show();
    }

    private void showAssignedDevices(UserInfo selectedUser) {
            if (selectedUser != null) {
                DataRepository dataRepository = DataRepository.getInstance();
                ArrayList<String> assignedDevices = dataRepository.getAssignedDevices(selectedUser.getUserName());

        
                // Display assigned devices in the text area
                StringBuilder deviceInfo = new StringBuilder();

                for (String deviceName : assignedDevices) {
                    int deviceIndex = dataRepository.getDeviceNames().indexOf(deviceName);
                    if (deviceIndex >= 0 && deviceIndex < dataRepository.getDeviceDetails().size()) {
                        String deviceDetail = dataRepository.getDeviceDetails().get(deviceIndex);
                        deviceInfo.append(deviceDetail).append("\n\n");
                    }

                    deviceInfoTextArea.setText(deviceInfo.toString());
                    selectedUserIndex = userList.getSelectionModel().getSelectedIndex();
                } 
            }
        }


        private void handleRemove() {
            int selectedIndex = userList.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                selectedUserIndex = selectedIndex;
        
                if (removeConfirmation(selectedIndex, "User")) {
                    DataRepository dataRepository = DataRepository.getInstance();
                    dataRepository.removeUser(selectedIndex);
        
                    userList.getItems().remove(selectedIndex);
                    deviceInfoTextArea.clear();
        
                    selectedUserIndex = -1;
        
                    // Save changes to file
                    dataRepository.saveUsersToFile();
                    dataRepository.saveUserAssignmentsToFile();
                }
            }
        }
        

        private boolean removeConfirmation(int selectedIndex, String userName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Remove user");
        alert.setContentText("Are you sure you want to remove " + userName + "?");
    
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
        }
        
    }



