package sample;

import com.sun.glass.ui.ClipboardAssistance;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;

public class Main extends Application {

    private AnimationTimer timer;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStage(primaryStage);
        controller.setActivity(this);

        primaryStage.setTitle("Copy Cat");
        primaryStage.setScene(new Scene(root, 299, 700));
        primaryStage.setResizable(false);
        primaryStage.show();

        controller.configColumns();

        //configClipboard(controller);

    }

    public static void main(String[] args) {
        launch(args);
    }

    /** ----- CONFIG ----- */

    private void configClipboard(Controller controller) {

        timer = new AnimationTimer() {

            Clipboard systemClipboard = Clipboard.getSystemClipboard();

            @Override
            public void handle(long timestamp) {
                String content = systemClipboard.getString();

                if (content != null) {
                    controller.setData(content);
                }

            }

        };

        timer.start();

    }

    public void shutDownTimer() {
        timer.stop();
    }

    public void startTimer() {
        timer.start();
    }

}
