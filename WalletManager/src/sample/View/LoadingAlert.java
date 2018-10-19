package sample.View;

import javafx.scene.control.Alert;

public class LoadingAlert {

    public static LoadingAlert instance = null;
    private Alert alert;

    public static LoadingAlert getInstance() {

        if (instance == null) {
            instance = new LoadingAlert();
        }

        return instance;
    }

    public LoadingAlert() {

        alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText("Loading...");

    }


    public void show() {

        alert.showAndWait();
    }

    public void dismiss() {
        alert.close();
    }

}
