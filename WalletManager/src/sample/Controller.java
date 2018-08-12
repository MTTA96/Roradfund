package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import sample.Interface.RequestBalanceByAddressCallBack;
import sample.Model.ETHScanner.Wallet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.Timer;

public class Controller implements RequestBalanceByAddressCallBack {

    /**
     * ----- VIEW -----
     */

    @FXML
    private TextField txfFilePath;
    @FXML
    private Label lblCountedWallet, lblSum;
    @FXML
    private Button btnOpenResFile, btnCheck;
    @FXML
    private TableView<Wallet> tbvResults;
    @FXML
    private TableColumn<Wallet, String> addressCol, balanceCol;
    @FXML
    private TableColumn<Wallet, Integer> serialCol;

    /**
     * ----- PROPS -----
     */

    private final ObservableList<Wallet> data = FXCollections.observableArrayList();
    private ArrayList<String> addressList = new ArrayList<>();
    private int countWallet = 0;
    private int showedWallets = 0;
    private Timer timer = new Timer();
    private Double sum = 0d;
    private boolean isChecking = false;
    private boolean isStopped = false;

    //0x3750fC1505ba9a4cA3907b94Cda8e5758d31F3aD

    /**
     * CONFIG
     */

    private void configColumns() {

        serialCol.setCellValueFactory(new PropertyValueFactory<Wallet, Integer>("serial"));

        addressCol.setCellValueFactory(new PropertyValueFactory<Wallet, String>("account"));

        balanceCol.setCellValueFactory(new PropertyValueFactory<Wallet, String>("balance"));

    }

    /**
     * ----- ACTIONS -----
     */

    @FXML
    void openResourceFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file");
        //fileChooser.showOpenDialog(null);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            txfFilePath.setText(file.getPath());
            handleFile(file);
        }

    }

    @FXML
    void checkBalance(ActionEvent event) {

        if (!isChecking)
            start();
        else
            stop();

    }

    @FXML
    void delete(ActionEvent event) {

        resetState();

    }

    private void start() {

        // Prepare data

        if (addressList.size() != 0) {

            System.out.print("Start\n");

            isChecking = true;
            isStopped = false;
            countWallet = 0;
            data.clear();
            tbvResults.setItems(data);
            btnCheck.setText("Dừng");

            lblCountedWallet.setText(countWallet + "/" + addressList.size());
            lblSum.setText(String.valueOf(sum));

            configColumns();

            callAPICheckBalance();

        }

    }

    private void stop() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btnCheck.setText(isStopped ? "Dừng" : "Tiếp tục");

                if (!isStopped) {
                    System.out.print("Stop\n");
                    timer.cancel();
                    timer = null;
                } else {
                    System.out.print("Continue\n");
                    callAPICheckBalance();
                }

                isStopped = !isStopped;
            }
        });

    }

    private void handleFile(File file) {

        resetState();

        try {
            String fileType = Files.probeContentType(file.toPath());

            switch (fileType) {
                // Word
                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                    handleWord(file);
                    break;

                // Excel
                case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                    break;

                // Text
                case "text/plain":
                    handleTxt(file);
                    break;

                default:
                    System.out.print("Failed!");
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    //alert.setTitle("File type warning!!!");
                    alert.setHeaderText("File type warning!!!");
                    alert.setContentText("Only handle excel, word or text file");

                    alert.showAndWait();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleWord(File file) {

        try {

            // Prepare file

            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XWPFDocument document = new XWPFDocument(fis);

            List<XWPFParagraph> paragraphs = document.getParagraphs();

            System.out.println("Total no of paragraph " + paragraphs.size());

            // Handle wallet list

            for (XWPFParagraph para : paragraphs) {

                if (!para.getText().isEmpty()) {
                    addressList.add(para.getText());
                }

            }
            lblCountedWallet.setText(String.valueOf(addressList.size()));

            fis.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void handleTxt(File file) {

        try (Scanner scanner = new Scanner(file)) {
            int i = 0;
            while (scanner.hasNext()) {
                String walletAddress = scanner.next();
                //System.out.println(walletAddress);
                addressList.add(walletAddress);
                i++;
            }

            lblCountedWallet.setText(String.valueOf(i));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void resetState() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timer.cancel();
            }
        });

        btnCheck.setText("Bắt đầu");
        lblCountedWallet.setText("");
        lblSum.setText("");
        isChecking = false;
        isStopped = true;
        addressList.clear();
        countWallet = 0;
        showedWallets = 0;
        sum = 0d;
        data.clear();
        tbvResults.setItems(data);

    }

    private void callAPICheckBalance() {

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                // If reach the last wallet then cancel request

                if (countWallet < addressList.size()) {

                    // Format address list

                    String addressesString = "";

                    int calculatedCounting = (countWallet + 20 < addressList.size() ? countWallet + 20 : countWallet + (addressList.size() - countWallet));

                    for (int i = countWallet; i < calculatedCounting; i++) {
                        if (i == calculatedCounting - 1)
                            addressesString += addressList.get(i);
                        else
                            addressesString += addressList.get(i) + ",";
                    }

                    showedWallets = countWallet;
                    countWallet += countWallet + 20 < addressList.size() ? 20 : addressList.size() - countWallet;

                    // Call API
                    Wallet.checkBalance(addressesString, Controller.this);

                } else {
                    this.cancel();
                }

            }
        }, 0, 5000);

    }

    private void updateCountingLabel() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblCountedWallet.setText(countWallet + "/" + addressList.size());
                lblSum.setText(String.valueOf(sum));
            }
        });
    }

    /**
     * ----- HANDLE RESULTS -----
     */

    @Override
    public void balanceByAddressCallBack(int errorCode, ArrayList<Wallet> wallet) {

        if (isStopped) {
            System.out.print("Stop plzzz");
            countWallet = showedWallets;
            return;
        }

        updateBalanceCol(wallet);

    }

    private void updateBalanceCol(ArrayList<Wallet> walletList) {

        if (walletList == null) {
            walletList = new ArrayList<>();
            Wallet errorWallet = new Wallet();
            errorWallet.setAccount("Error!");
            walletList.add(errorWallet);
        }

        for (int i = 0; i < walletList.size(); i++) {
            walletList.get(i).setSerial((countWallet - (walletList.size() - i)) + 1);
        }

        // Handle when finish checking

        if (countWallet == addressList.size()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.print("Finish\n");
                    btnCheck.setText("Bắt đầu");
                }
            });
            isChecking = false;
            isStopped = true;
        }

        sum += Wallet.sum(walletList);
        System.out.print(sum);
        data.addAll(walletList);

        // Update UI
        tbvResults.setItems(data);
        updateCountingLabel();

    }

}
