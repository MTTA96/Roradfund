package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import sample.Model.TableData;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    @FXML
    private TextArea txaData;
    @FXML
    private TableView<TableData> tbvData;

    @FXML
    private TableColumn<TableData, String> colData;

    @FXML
    private Button btnMakeOnTop, btnStart, btnTab;

    private Stage stage;
    private Main main;
    private boolean isAlwaysOnTop = false;
    private Robot robot = null;
    private String oldData = "";
    private final ObservableList<TableData> dataList = FXCollections.observableArrayList();
    private boolean isCopiedFromTable = false;
    private boolean isStarted = true;
    private boolean isAutoTab = true;
    private int delayTime = 200;
    private int delayTimeForDelete = 50;

    /** ----- CONFIG ----- */

    public void configColumns() {

        colData.setCellValueFactory(new PropertyValueFactory<TableData, String>("data"));

        /** Action */

        tbvData.setRowFactory(tv -> {
            TableRow<TableData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()== MouseButton.PRIMARY && event.getClickCount() == 1) {

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

//                    btnStart.setText("Stop");
//                    isStarted = false;
//                    main.shutDownTimer();
                    //isCopiedFromTable = true;
                    saveToClipboard(clipboardString.toString());

                    if (setUpRobot()) {

                        // Alt + Tab
                        triggerAltTabKeys();
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {

                                // Paste
                                triggerPasteKey();

                                if (isAutoTab) {
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            triggerTabKey();
                                        }
                                    }, delayTimeForDelete);
                                }

                            }
                        }, delayTime);

                    }

                }
            });
            return row;
        });

    }

    /** ----- ACTION ----- */

    @FXML
    void onDataChanged(InputMethodEvent event) {

        if (setUpRobot()) {
            setData(txaData.getText());
        }

    }

    @FXML
    void start(ActionEvent event) {

//        if (isStarted) {
//            btnStart.setText("Stop");
//            main.shutDownTimer();
//        } else {
//            btnStart.setText("Start");
//            main.startTimer();
//        }
//
//        isStarted = !isStarted;

    }

    @FXML
    void pasteToTable(ActionEvent event) {

        setData(Clipboard.getSystemClipboard().getString());

    }

    @FXML
    void bringToFront(ActionEvent event) {
        if(stage != null) {
            isAlwaysOnTop = !isAlwaysOnTop;
            btnMakeOnTop.setText(isAlwaysOnTop ? "Move to back" : "Move to front");
            stage.setAlwaysOnTop(isAlwaysOnTop);
        }
    }

    @FXML
    void delete(ActionEvent event) {

        if (robot == null) {

            if(setUpRobot()) {
                triggerAltTabKeys();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerDelete();
                    }
                }, delayTime);

            }

        } else {

            triggerAltTabKeys();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    triggerDelete();
                }
            }, delayTime);

        }

    }

    @FXML
    void turnOnTab(ActionEvent event) {

        isAutoTab = !isAutoTab;

        if (isAutoTab) {
            btnTab.setText("Tab");
        } else {
            btnTab.setText("No Tab");
        }

    }

    /** ----- SUPPORTED FUNCS ----- */

    public void setActivity(Main main) {
        this.main = main;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setData(String data) {

        if (!oldData.equals(data)) {

            //if (!isCopiedFromTable) {

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

            //}

        }

    }

    public boolean setUpRobot() {
        try {
            robot = new Robot();
            return true;
        } catch (AWTException awte) {
            awte.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, awte.getLocalizedMessage(), null);
            alert.show();
            return false;
        }
    }

    private void saveToClipboard(String content) {

        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(content);
        Clipboard.getSystemClipboard().setContent(clipboardContent);

    }

    private void triggerAltTabKeys() {

        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_TAB);

        // Release paste event

        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_TAB);

    }

    private void triggerTabKey() {

        robot.keyPress(KeyEvent.VK_TAB);

        robot.keyRelease(KeyEvent.VK_TAB);

    }

    private void triggerPasteKey() {

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);

        // Release paste event

        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);

    }

    private void triggerDelete() {

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);

        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_A);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                robot.keyPress(KeyEvent.VK_DELETE);
                robot.keyRelease(KeyEvent.VK_DELETE);
            }
        }, delayTimeForDelete);

    }

}
