import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ClientGUI extends Application {

    private PrintWriter writer;
    private BufferedReader reader;

    private VBox messageContainer;
    private ScrollPane scrollPane;

    // NEW: The list of active users
    private ListView<String> userList;
    private ObservableList<String> usersObservable;

    private String username;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Login");
        dialog.setHeaderText("Welcome to ChatApp");
        dialog.setContentText("Username:");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);

        var result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            username = result.get().trim();
        } else {
            return;
        }

        primaryStage.setTitle("Chat - " + username);

        // --- 1. LEFT SIDE: USER LIST ---
        usersObservable = FXCollections.observableArrayList();
        userList = new ListView<>(usersObservable);
        userList.setPrefWidth(120);
        userList.setStyle("-fx-background-color: #f0f2f5; -fx-border-color: #ddd;");

        VBox leftBox = new VBox(new Label("Online Users"), userList);
        leftBox.setPadding(new Insets(10));
        leftBox.setStyle("-fx-background-color: #ffffff;");

        // --- 2. CENTER: CHAT AREA ---
        messageContainer = new VBox(10);
        messageContainer.setPadding(new Insets(10));

        scrollPane = new ScrollPane(messageContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVvalue(1.0);

        // --- 3. BOTTOM: INPUT ---
        TextField inputField = new TextField();
        inputField.setPromptText("Type a message...");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        Button btnSend = new Button("➤");
        HBox inputBox = new HBox(10, inputField, btnSend);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getStyleClass().add("input-box");

        btnSend.setOnAction(e -> sendMessage(inputField));
        inputField.setOnAction(e -> sendMessage(inputField));

        // --- LAYOUT ---
        BorderPane root = new BorderPane();
        root.setLeft(leftBox); // Add the list to the left
        root.setCenter(scrollPane);
        root.setBottom(inputBox);

        Scene scene = new Scene(root, 550, 500); // Made it slightly wider
        try { scene.getStylesheets().add(getClass().getResource("chat.css").toExternalForm()); }
        catch (Exception e) {/*ignore*/}

        primaryStage.setScene(scene);
        primaryStage.show();

        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 1234);
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                writer.println(username);

                String msg;
                while ((msg = reader.readLine()) != null) {
                    String finalMsg = msg;
                    Platform.runLater(() -> processIncomingMessage(finalMsg));
                }
            } catch (IOException e) {
                Platform.runLater(() -> addSystemMessage("Error: Server not found."));
            }
        }).start();
    }

    // --- NEW LOGIC: Distinguish Chat vs User List ---
    private void processIncomingMessage(String msg) {
        if (msg.startsWith("USERS:")) {
            // It's a list update! "USERS:Alice,Bob,Charlie,"
            String cleanMsg = msg.replace("USERS:", "");
            String[] names = cleanMsg.split(",");

            // Update the sidebar list
            usersObservable.clear();
            usersObservable.addAll(Arrays.asList(names));

        } else if (msg.startsWith("SERVER:")) {
            addSystemMessage(msg.replace("SERVER: ", ""));
        } else if (msg.contains(": ")) {
            String[] parts = msg.split(": ", 2);
            if (!parts[0].equals(username)) {
                addBubble(parts[0] + "\n" + parts[1], false);
            }
        }
    }

    private void sendMessage(TextField input) {
        String msg = input.getText().trim();
        if (!msg.isEmpty() && writer != null) {
            writer.println(username + ": " + msg);
            addBubble(msg, true);
            input.clear();
        }
    }

    private void addBubble(String text, boolean isMyMessage) {
        Label bubble = new Label(text);
        bubble.setWrapText(true);
        bubble.setMaxWidth(250);
        bubble.getStyleClass().add("chat-bubble");
        bubble.getStyleClass().add(isMyMessage ? "my-message" : "other-message");

        HBox container = new HBox(bubble);
        container.setAlignment(isMyMessage ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messageContainer.getChildren().add(container);

        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }

    private void addSystemMessage(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 10px; -fx-text-fill: #555; -fx-padding: 5px;");
        HBox container = new HBox(label);
        container.setAlignment(Pos.CENTER);
        messageContainer.getChildren().add(container);
    }
}