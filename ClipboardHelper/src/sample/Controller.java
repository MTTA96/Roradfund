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
import java.util.*;
import java.util.List;

public class Controller {

    @FXML
    private TextArea txaData;
    @FXML
    private TableView<TableData> tbvData;

    @FXML
    private TableColumn<TableData, String> colData, col1;

    @FXML
    private Button btnMakeOnTop, btnStart, btnTab, btnAddComment;

    private Stage stage;
    private Main main;
    private boolean isAlwaysOnTop = false;
    private Robot robot = null;
    private String oldData = "";
    private ArrayList<String> dataCol1 = new ArrayList<>();
    private ArrayList<String> dataCol2 = new ArrayList<>();
    private final ObservableList<TableData> dataList = FXCollections.observableArrayList();
    private boolean isCopiedFromTable = false;
    private boolean isStarted = true;
    private boolean isAutoTab = true;
    private int delayTime = 200;
    private int delayTimeForDelete = 50;

    private List<String> commentList = Arrays.asList(
            "I'm looking for this",
            "So awesome man",
            "This is good project",
            "I think its really good",
            "This project is good",
            "Now its looking for ppl",
            "I think is good",
            "I'm done it",
            "Awesome!",
            "Whoa, this is a really cool contest!",
            "Awesome man",
            "Coolest contest I've ever seen!",
            "Good guys",
            "Looking good",
            "Not bad",
            "This contest is really awesome. Hope I win!",
            "So awesome!",
            "Nice",
            "Nice Project",
            "Good Project",
            "Just signed up to the contest!",
            "Really nice",
            "Cool Project",
            "Not bad guys",
            "So cool",
            "Love it",
            "wow amazing project",
            "Join it guys",
            "This so cool",
            "It's not so bad",
            "Give me more");
    private Random rand = new Random();

    /** ----- CONFIG ----- */

    public void configColumns() {

        colData.setCellValueFactory(new PropertyValueFactory<TableData, String>("data2"));
        col1.setCellValueFactory(new PropertyValueFactory<TableData, String>("data1"));

        col1.setSortable(false);
        colData.setSortable(false);
        col1.setStyle("-fx-font-size:16pt; -fx-font-weight:bold;");

        setUpRobot();
        /** Action */

        tbvData.setRowFactory(tv -> {
            TableRow<TableData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {

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

//                                if (isAutoTab) {
//                                    Timer timer = new Timer();
//                                    timer.schedule(new TimerTask() {
//                                        @Override
//                                        public void run() {
//                                            triggerTabKey();
//                                        }
//                                    }, delayTimeForDelete);
//                                }
                            }
                        }, delayTime);

                    }

                } else {
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

                                // Tab
                                Timer timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        triggerTabKey();
                                    }
                                }, delayTimeForDelete);
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
    void addComment(ActionEvent event) {

        triggerAltTabKeys();
        int randomIndex = rand.nextInt(commentList.size());
        saveToClipboard(commentList.get(randomIndex));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                triggerPasteKey();
            }
        }, delayTime);

    }

    @FXML
    void pasteToCol1(ActionEvent event) {

        setDataCol1(Clipboard.getSystemClipboard().getString());

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

    public void setDataCol1(String data) {

        String[] tempList = data.split("\r\n");
        dataList.clear();
        dataCol1.clear();
        tbvData.getItems().clear();

        int index = 0;
        for (String line: tempList) {
            if(!line.equals("") && !line.equals(" ")) {
                //System.out.print(line + "\n");
                dataCol1.add(line.replace("\r\n", ""));
                dataList.add(new TableData(line.replace("\r\n", ""), index < dataCol2.size() ? dataCol2.get(index) : ""));
                index += 1;
            }
        }

        System.out.print("Update col1: current index " + index + " - Col2 " + dataCol2.size() + "\n");

        if(index < dataCol2.size()) {

            System.out.print("Update more for col2 \n");
            for (int i = index; i < dataCol2.size(); i++) {
                dataList.add(new TableData("", dataCol2.get(i)));
            }
        }

        tbvData.setItems(dataList);

    }

    public void setData(String data) {

        if (!oldData.equals(data)) {

            //if (!isCopiedFromTable) {

            oldData = data;
            String[] tempList = data.split("\r\n");
            dataList.clear();
            dataCol2.clear();
            tbvData.getItems().clear();
            int index = 0;
            for (String line : tempList) {
                if (!line.equals("") && !line.equals(" ")) {
                    //System.out.print(line + "\n");
                    dataCol2.add(line.replace("\r\n", ""));
                    dataList.add(new TableData(index < dataCol1.size() ? dataCol1.get(index) : "", line.replace("\r\n", "")));
                    index += 1;
                }
            }

            System.out.print("Update col2: current index " + index + " - Col1 " + dataCol1.size() + "\n");

            if(index < dataCol1.size()) {
                System.out.print("Update more for col1 \n");
                for (int i = index; i < dataCol1.size(); i++) {
                    dataList.add(new TableData(dataCol1.get(i), ""));
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
