package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import sample.Model.TableData;
import sun.java2d.SurfaceData;
import sun.lwawt.LWWindowPeer;
import sun.lwawt.PlatformWindow;

import java.awt.*;
import java.awt.MenuBar;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Controller {

    @FXML
    private TableView<TableData> tbvData;

    @FXML
    private TableColumn<TableData, String> colData;

    @FXML
    private Button btnMakeOnTop;

    private Stage stage;
    private boolean isAlwaysOnTop = false;
    private String oldData = "";
    private final ObservableList<TableData> dataList = FXCollections.observableArrayList();

    /** ----- CONFIG ----- */

    public void configColumns() {

        colData.setCellValueFactory(new PropertyValueFactory<TableData, String>("data"));

        /** Action */

        tbvData.setRowFactory(tv -> {
            TableRow<TableData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()== MouseButton.PRIMARY && event.getClickCount() == 2) {

                    // Copy row's content

                    ObservableList<TablePosition> posList = tbvData.getSelectionModel().getSelectedCells();
                    int old_r = -1;
                    StringBuilder clipboardString = new StringBuilder();
                    for (TablePosition p : posList) {
                        int r = p.getRow();
                        int c = p.getColumn();
                        Object cell = tbvData.getColumns().get(c).getCellData(r);
                        if (cell == null)
                            cell = "";
                        if (old_r == r)
                            clipboardString.append('\t');
                        else if (old_r != -1)
                            clipboardString.append('\n');
                        clipboardString.append(cell);
                        old_r = r;
                    }
                    
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(clipboardString.toString());
                    Clipboard.getSystemClipboard().setContent(content);

                    // Trigger paste event

                    Robot robot = null;
                    try {
                        robot = new Robot();
                    } catch (AWTException awte) {
                        awte.printStackTrace();
                    }
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_V);

                    // Release paste event

                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    robot.keyRelease(KeyEvent.VK_V);

                }
            });
            return row;
        });

    }

    /** ----- ACTION ----- */

    @FXML
    void bringToFront(ActionEvent event) {
        if(stage != null) {
            isAlwaysOnTop = !isAlwaysOnTop;
            btnMakeOnTop.setText(isAlwaysOnTop ? "Move to back" : "Move to front");
            stage.setAlwaysOnTop(isAlwaysOnTop);
        }
    }

    /** ----- SUPPORTED FUNCS ----- */

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setData(String data) {

        if (!oldData.equals(data)) {
            oldData = data;
            String[] tempList = data.split("\n");
            dataList.clear();
            tbvData.getItems().clear();
            for (String line:
                 tempList) {
                if(!line.equals("") && !line.equals(" ")) {
                    System.out.print(line + "\n");
                    dataList.add(new TableData(line));
                }
            }

            tbvData.setItems(dataList);
        }

    }

}
