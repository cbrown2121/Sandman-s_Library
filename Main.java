import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DataRepository dataRepository = DataRepository.getInstance();
        dataRepository.loadNamesFromFile();
        primaryStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);

        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 1);

        TextField usernameTextField = new TextField();
        grid.add(usernameTextField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        Button loginButton = new Button("Login");
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(loginButton);
        grid.add(hBox, 1, 4);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 6);

        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            if (authenticate(username, password)) {
                actionTarget.setText("Login successful");
                openMainMenu(primaryStage);
            } else {
                actionTarget.setText("Login failed");
            }
        });

        primaryStage.show();
    }

    private boolean authenticate(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }

    private void openMainMenu(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(primaryStage);
    }
}
