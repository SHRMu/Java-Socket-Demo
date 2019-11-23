import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Application {

    private String name = "client";

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Socket server = new Socket(InetAddress.getLocalHost(), 8899);
        BufferedReader br = new BufferedReader(new InputStreamReader(server.getInputStream()));
        ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

        BorderPane pane = new BorderPane();
        TextArea textArea = new TextArea();
        Runnable runnable = () ->{
            String msg;
            while (true){
                try {
                    if ((msg = br.readLine()) != null){
                        textArea.appendText(msg + "\n");
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        Thread client = new Thread(runnable);
        client.start();

        TextField inputMsg = new TextField();
        Button sendBtn = new Button("send");

        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String content = inputMsg.getText();
                inputMsg.setText("");
                try {
                    oos.writeObject(new Message(name, content));
                    oos.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

        });

        pane.setCenter(textArea);
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().add(sendBtn);
        box.getChildren().add(inputMsg);

        pane.setBottom(box);
        Scene scene = new Scene(pane, 275, 245);
        stage.setScene(scene);
        stage.show();
    }
}
