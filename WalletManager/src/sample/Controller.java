package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import sample.Interface.RequestWalletEthplorerInfoCallBack;
import sample.Model.ETHScanner.WalletScan;
import sample.Model.Ethplorer.TableWalletEthplorer;
import sample.Model.Ethplorer.Token;
import sample.Model.Ethplorer.WalletETHplorer;
import sample.Model.Symbol.Symbol;
import sample.Model.Symbol.SymbolList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.Timer;

public class Controller implements RequestWalletEthplorerInfoCallBack {

    /**
     * ----- VIEW -----
     */

    @FXML
    private TextField txfFilePath;
    @FXML
    private Label lblCountedWallet, lblSum, lblErrorWallet;
    @FXML
    private Button btnOpenResFile, btnCheck;

    @FXML
    private TableView<TableWalletEthplorer> tbvTotalResults;
    @FXML
    private TableColumn<TableWalletEthplorer, Integer> colSerialResult;
    @FXML
    private TableColumn<TableWalletEthplorer, String> colSymbolTokenResult;
    @FXML
    private TableColumn<TableWalletEthplorer, String> colTokenNameResult;
    @FXML
    private TableColumn<TableWalletEthplorer, Integer> colNumberOfTokenResult;
    @FXML
    private TableColumn<TableWalletEthplorer, Integer> colNumberOfWalletResult;
    @FXML
    private TableColumn<TableWalletEthplorer, Double> colSumResult;

    @FXML
    private TableView<WalletScan> tbvBalanceResults;
    @FXML
    private TableColumn<WalletScan, String> addressCol, balanceCol, colTokenAmountDetails;
    @FXML
    private TableColumn<WalletScan, Integer> serialCol;

    /**
     * ----- PROPS -----
     */

    private WalletETHplorer ethplorerWallet;
    private final ObservableList<WalletScan> data = FXCollections.observableArrayList();
    private final ObservableList<TableWalletEthplorer> dataResults = FXCollections.observableArrayList();
    private ArrayList<String> addressList = new ArrayList<>();
    private ArrayList<SymbolList> symbolLists = new ArrayList<>();
    private Timer timer = new Timer();
    private Timer timerResults = new Timer();

    /** Supported props */

    private int countWallet = 0;
    private int showedWallets = 0;
    private Double sum = 0d;
    private boolean isChecking = false;
    private boolean isStopped = false;

    //0x3750fC1505ba9a4cA3907b94Cda8e5758d31F3aD

    /**
     * CONFIG
     */

    private void configColumns() {

        /** Result cols */

        colSerialResult.setCellValueFactory(new PropertyValueFactory<TableWalletEthplorer, Integer>("serial"));
        colSymbolTokenResult.setCellValueFactory(new PropertyValueFactory<TableWalletEthplorer, String>("symbol"));
        colTokenNameResult.setCellValueFactory(new PropertyValueFactory<TableWalletEthplorer, String>("tokenName"));
        colNumberOfTokenResult.setCellValueFactory(new PropertyValueFactory<TableWalletEthplorer, Integer>("numberOfToken"));
        colNumberOfWalletResult.setCellValueFactory(new PropertyValueFactory<TableWalletEthplorer, Integer>("numberOfWallet"));
        colSumResult.setCellValueFactory(new PropertyValueFactory<TableWalletEthplorer, Double>("sum"));

        /** Details cols */

        serialCol.setCellValueFactory(new PropertyValueFactory<WalletScan, Integer>("serial"));
        addressCol.setCellValueFactory(new PropertyValueFactory<WalletScan, String>("account"));
        colTokenAmountDetails.setCellValueFactory(new PropertyValueFactory<WalletScan, String>("tokenAmount"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<WalletScan, String>("balance"));

        /** Action */

        tbvTotalResults.setRowFactory(tv -> {
            TableRow<TableWalletEthplorer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 2) {

                    TableWalletEthplorer clickedRow = row.getItem();
                    System.out.print(clickedRow);

                    data.removeAll();
                    tbvBalanceResults.getItems().clear();
//                    balanceCol.
                    for (SymbolList symbolList :
                            symbolLists) {
                        if (symbolList.getSymbolName().equals(clickedRow.getSymbol())) {

                            int count = 1;
                            for (Token token : symbolList.getTokenList()) {
                                data.add(new WalletScan(count, token.getTokenInfo().getAddress(), token.getBalance().toString(), String.valueOf(0)));
                                count += 1;
                            }
                            tbvBalanceResults.setItems(data);
                        }
                    }

                }
            });
            stop();
            return row;
        });


        /** Copy row's content */

        tbvBalanceResults.getSelectionModel().setCellSelectionEnabled(true);
        tbvBalanceResults.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        MenuItem item = new MenuItem("Copy");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<TablePosition> posList = tbvBalanceResults.getSelectionModel().getSelectedCells();
                int old_r = -1;
                StringBuilder clipboardString = new StringBuilder();
                for (TablePosition p : posList) {
                    int r = p.getRow();
                    int c = p.getColumn();
                    Object cell = tbvBalanceResults.getColumns().get(c).getCellData(r);
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
            }
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(item);
        tbvBalanceResults.setContextMenu(menu);

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
            tbvBalanceResults.setItems(data);
            btnCheck.setText("Dừng");

            lblCountedWallet.setText(countWallet + "/" + addressList.size());
            lblSum.setText(String.valueOf(sum));

            configColumns();

            //callAPICheckBalance();
            callAPIGetWalletInfo();

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
                    //callAPICheckBalance();
                    callAPIGetWalletInfo();
                }

                isStopped = !isStopped;
            }
        });

    }

    /** ----- SUPPORTED FUNC ----- */

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
        tbvBalanceResults.setItems(data);

    }

    private int existedSymbol(String symbol) {

//        for (Symbol symbol1 : symbolList.getTokenList()) {
//            if (symbol1.getSymbol().equals(symbol)) {
//                return true;
//            }
//        }

//        //int i = -1;
//        for (SymbolList symbolList: symbolList) {
//            i+=1;
//            if (symbolList.getSymbolName().equals(symbol)) {
//                return i;
//            }
//        }

        for (int i = 0; i < symbolLists.size(); i++) {
            if (symbolLists.get(i).getSymbolName().equals(symbol)) {
                return i;
            }
        }

        return -1;

    }

    private void setUpDataResultTable(TableWalletEthplorer tableWalletEthplorer, List<Token> tokenList, Token token) {

        SymbolList symbolList = new SymbolList();
        symbolList.setSymbolName(token.getTokenInfo().getSymbol());
        symbolList.setTotal(token.getBalance());
        symbolList.getTokenList().addAll(tokenList);

        this.symbolLists.add(symbolList);

        // Prepare table data

        tableWalletEthplorer.setSymbol(symbolList.getSymbolName());
        tableWalletEthplorer.setSum(symbolList.getTotal());
        tableWalletEthplorer.setNumberOfWallet(symbolLists.get(0).getTokenList().size());

        dataResults.add(tableWalletEthplorer);

        // Update UI
        tbvTotalResults.setItems(dataResults);

    }

    private void updateBalanceCol(WalletETHplorer wallet) {

        WalletScan walletScan = new WalletScan();

        if (wallet == null) {
            walletScan.setAccount("Error!");
        } else {
            walletScan.setSerial(countWallet);
            walletScan.setAccount(wallet.getAddress().isEmpty() ? "Không thể lấy địa chỉ" : wallet.getAddress());
            walletScan.setBalance(wallet.getETH().getBalance().toString());
        }

        // Handle when finish checking

        if (countWallet > addressList.size()) {
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

        sum += Double.parseDouble(walletScan.getBalance());
        System.out.print(sum);
        data.add(walletScan);

        // Update UI
        tbvBalanceResults.setItems(data);
        updateCountingLabel();

    }

    private void updateResultCols(WalletETHplorer wallet) {

        TableWalletEthplorer tableWalletEthplorer = new TableWalletEthplorer();
        tableWalletEthplorer.setSerial(symbolLists.size());

        int symbolIndex = -1;

        if (wallet.getTokens() != null) {

            // Check symbol
            if (symbolLists.size() == 0) {

                for (int i = 0; i < wallet.getTokens().size(); i++) {

                    Token token = wallet.getTokens().get(i);

                    setUpDataResultTable(tableWalletEthplorer, wallet.getTokens(), token);

                }

                return;
            }

            for (int i = 0; i < wallet.getTokens().size(); i++) {

                Token token = wallet.getTokens().get(i);

                // Check if symbol isn't existed in the list then add to the list

                symbolIndex = existedSymbol(wallet.getTokens().get(i).getTokenInfo().getSymbol());
                if (symbolIndex == -1) {

                    setUpDataResultTable(tableWalletEthplorer, wallet.getTokens(), token);

                } else {

                    // Add all token address to the list
                    symbolLists.get(symbolIndex).getTokenList().addAll(wallet.getTokens());
                }

            }

        }

        // Handle when finished checking

        if (countWallet > addressList.size()) {
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

    }

    /** ----- API SERVICES ----- */

    private void callAPIGetWalletInfo() {

        // If reach the last wallet then cancel request

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (countWallet < addressList.size()) {

                    // Format address list

                    String address = addressList.get(countWallet);
                    countWallet += 1;
                    //countWallet += countWallet + 1 < addressList.size() ? 1 : addressList.size() - countWallet;

                    // Call API
                    WalletETHplorer.getWalletInfo(address, Controller.this);

                }
            }
        }, 6000);

    }

//    private void callAPICheckBalance() {
//
//        timer = new Timer();
//
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//                // If reach the last wallet then cancel request
//
//                if (countWallet < addressList.size()) {
//
//                    // Format address list
//
//                    String addressesString = "";
//
//                    int calculatedCounting = (countWallet + 20 < addressList.size() ? countWallet + 20 : countWallet + (addressList.size() - countWallet));
//
//                    for (int i = countWallet; i < calculatedCounting; i++) {
//                        if (i == calculatedCounting - 1)
//                            addressesString += addressList.get(i);
//                        else
//                            addressesString += addressList.get(i) + ",";
//                    }
//
//                    showedWallets = countWallet;
//                    countWallet += countWallet + 20 < addressList.size() ? 20 : addressList.size() - countWallet;
//
//                    // Call API
//                    WalletScan.checkBalance(addressesString, Controller.this);
//
//                } else {
//                    this.cancel();
//                }
//
//            }
//        }, 0, 5000);
//
//    }

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

//    @Override
//    public void balanceByAddressCallBack(int errorCode, ArrayList<WalletScan> walletScan) {
//
//        if (isStopped) {
//            System.out.print("Stop plzzz");
//            countWallet = showedWallets;
//            return;
//        }
//
//        updateBalanceCol(walletScan);
//
//    }

    @Override
    public void walletInfoCallBack(int errorCode, WalletETHplorer wallet) {

        if (isStopped) {
            System.out.print("Stop plzzz");
            countWallet = showedWallets;
            return;
        }

        updateBalanceCol(wallet);
        updateResultCols(wallet);
        callAPIGetWalletInfo();

    }

}
