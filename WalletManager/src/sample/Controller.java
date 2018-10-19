package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.internal.jline.internal.Nullable;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import sample.Interface.RequestGasCallBack;
import sample.Interface.RequestWalletEthplorerInfoCallBack;
import sample.Interface.SendETHCallBack;
import sample.Model.ETHScanner.WalletScan;
import sample.Model.ETHTrader;
import sample.Model.Gas;
import sample.Model.MainTableModel;
import sample.Model.Ethplorer.Token;
import sample.Model.Ethplorer.WalletETHplorer;
import sample.Model.Symbol.SymbolList;
import sample.Util.SupportKeys;
import sample.View.LoadingAlert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.*;
import java.util.Timer;

public class Controller implements RequestWalletEthplorerInfoCallBack, RequestGasCallBack, SendETHCallBack {

    /** ----- VIEW ----- */

    @FXML
    private Tab tradingTab;

    /** CHECKING TAB */

    @FXML
    private TextField txfFilePath;
    @FXML
    private Label lblCountedWallet, lblSum, lblErrorWallet;
    @FXML
    private Button btnOpenResFile, btnCheck;

    @FXML
    private TableView<MainTableModel> tbvMain;
    @FXML
    private TableColumn<MainTableModel, Integer> colSerialMain, colNumberOfTokenMain, colNumberOfWalletMain;
    @FXML
    private TableColumn<MainTableModel, String> colSymbolTokenMain, colTokenNameMain;
    @FXML
    private TableColumn<MainTableModel, Double> colTotalMain;

    @FXML
    private TableView<WalletScan> tbvDetails;
    @FXML
    private TableColumn<WalletScan, String> colAddressDetails, colBalanceDetails, colTokenAmountDetails;
    @FXML
    private TableColumn<WalletScan, Integer> colSerialDetails;

    /** TRADING TAB */

    @FXML
    private TextField txfMainWalletFilePathTradingTab,
            txfOtherWalletsFilePathTradingTab,
            txfGasLimitTradingTab,
            txfGasPriceTradingTab,
            txfMainAddressTradingTab;
    @FXML
    private PasswordField pwfMainAddressPasswordTradingTab;
    @FXML
    private Label lblSelectedTokenTradingTab, lblBalanceMainWalletTradingTab, lblTotalWalletTradingTab, lblFeePerWalletTradingTab, lblTotalFeeTradingTab;
//    @FXML
//    private Button btnOpenResFileTradingTab, btnSendTradingTab;


    /** ----- PROPS ----- */

    private Stage stage;
    private final ObservableList<WalletScan> data = FXCollections.observableArrayList();
    private final ObservableList<MainTableModel> dataResults = FXCollections.observableArrayList();
    private ArrayList<String> addressList = new ArrayList<>();
    private ArrayList<WalletScan> walletList = new ArrayList<>();
    private ArrayList<SymbolList> symbolLists = new ArrayList<>();
    private Timer timer = new Timer();

    /** Supported props */

    private int totalWallet = 0;
    private int countWallet = 0;
    private int showedWallets = 0;
    private Double sum = 0d;
    private boolean isChecking = false;
    private boolean isStopped = false;
    private boolean chekingSingleWallet = false;
    
    private long delayTime = 6000;

    /** Trading tab */

    private ETHTrader trader;
    private WalletETHplorer mainWallet = null;
    private String selectedToken = "";
    private Double fee = 0d;
    private Double gasPrice = 0d;
    private int gasLimit = 21000;

    //0x3750fC1505ba9a4cA3907b94Cda8e5758d31F3aD


    public Controller() {}

    /** ----- CONFIG ----- */

    void configTabs() {
        configCheckingTab();
        configTradingTab();
    }

    private void configCheckingTab() {
        configColumns();
    }

    private void configColumns() {

        /** Main cols */

        colSerialMain.setCellValueFactory(new PropertyValueFactory<MainTableModel, Integer>("serial"));
        colSymbolTokenMain.setCellValueFactory(new PropertyValueFactory<MainTableModel, String>("symbol"));
        colTokenNameMain.setCellValueFactory(new PropertyValueFactory<MainTableModel, String>("tokenName"));
        colNumberOfTokenMain.setCellValueFactory(new PropertyValueFactory<MainTableModel, Integer>("numberOfToken"));
        colNumberOfWalletMain.setCellValueFactory(new PropertyValueFactory<MainTableModel, Integer>("numberOfWallet"));
        colTotalMain.setCellValueFactory(new PropertyValueFactory<MainTableModel, Double>("sum"));

        /** Details cols */

        colSerialDetails.setCellValueFactory(new PropertyValueFactory<WalletScan, Integer>("serial"));
        colAddressDetails.setCellValueFactory(new PropertyValueFactory<WalletScan, String>("account"));
        colTokenAmountDetails.setCellValueFactory(new PropertyValueFactory<WalletScan, String>("tokenAmount"));
        colBalanceDetails.setCellValueFactory(new PropertyValueFactory<WalletScan, String>("balance"));

        /** Action */

        tbvMain.setRowFactory(tv -> {
            TableRow<MainTableModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 2) {

                    MainTableModel rowData = row.getItem();
                    System.out.print(rowData);

                    data.removeAll();
                    tbvDetails.getItems().clear();

                    if (rowData.getSymbol().equals("All")) {
                        int count = 1;
                        for (WalletScan wallet : walletList) {
                            data.add(new WalletScan(count, wallet.getAccount(), wallet.getTokenAmount(), wallet.getBalance()));
                            count += 1;
                        }
                        tbvDetails.setItems(data);
                    } else

                        for (SymbolList symbolList : symbolLists) {
                            if (symbolList.getSymbolName().equals(rowData.getSymbol())) {

                                int count = 1;
                                for (Token token : symbolList.getTokenList()) {
                                    data.add(new WalletScan(count, token.getWalletAddress(), token.getBalance().toString(), String.valueOf(0)));
                                    count += 1;
                                }
                                lblTotalWalletTradingTab.setText(String.valueOf(count));
                                tbvDetails.setItems(data);

                            }
                        }

                        // Update trading tab data

                        selectedToken = rowData.getSymbol();
                        lblSelectedTokenTradingTab.setText(rowData.getTokenName() + " - " + rowData.getSymbol());

                }
            });
            return row;
        });


        /** Copy row's content */

        tbvDetails.getSelectionModel().setCellSelectionEnabled(true);
        tbvDetails.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        MenuItem item = new MenuItem("Copy");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<TablePosition> posList = tbvDetails.getSelectionModel().getSelectedCells();
                int old_r = -1;
                StringBuilder clipboardString = new StringBuilder();
                for (TablePosition p : posList) {
                    int r = p.getRow();
                    int c = p.getColumn();
                    Object cell = tbvDetails.getColumns().get(c).getCellData(r);
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
        tbvDetails.setContextMenu(menu);

    }

    /** TRADING TAB */

    private void configTradingTab() {

        trader = new ETHTrader();

        tradingTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                System.out.print("openTradingTab");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        totalWallet = addressList.size();
                        lblTotalWalletTradingTab.setText(String.valueOf(totalWallet));
                    }
                });
                checkGasPrice(null);
            }
        });

        /// ----- Need to be optimized

//        txfGasPriceTradingTab.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable,
//                                String oldValue, String newValue) {
//                if (!newValue.matches("\\d*")) {
//                    txfGasPriceTradingTab.setText(newValue.replaceAll("[^\\d]", ""));
//
//                    gasPrice = Float.valueOf(newValue);
//                    updateFee(Float.valueOf(newValue));
//                }
//            }
//        });
//
//        txfGasLimitTradingTab.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                gasLimit = Integer.valueOf(newValue);
//                updateFee(gasPrice);
//            }
//        });
        txfMainAddressTradingTab.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        lblBalanceMainWalletTradingTab.setText("Loading...");
                        requestSingleWalletInfo(newValue);
                    }
                });
            }
        });

    }

    /** ----- ACTIONS ----- */

    /** TRADING TAB */

    @FXML
    void openMainWalletFile(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file");
        //fileChooser.showOpenDialog(null);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            txfMainWalletFilePathTradingTab.setText(file.getPath());
//            handleFile(file);
        }

    }

    @FXML
    void openOtherWalletFile(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file");
        //fileChooser.showOpenDialog(null);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            txfOtherWalletsFilePathTradingTab.setText(file.getPath());
            handleFile(file);
        }

    }

    @FXML
    void checkGasPrice(ActionEvent event) {

        txfGasPriceTradingTab.setText("Loading...");
        lblFeePerWalletTradingTab.setText("Loading...");
        lblTotalFeeTradingTab.setText("Loading...");
        requestGasData();
    }

    @FXML
    void calculateGasPrice(ActionEvent event) {

        gasLimit = Integer.valueOf(txfGasLimitTradingTab.getText());
        gasPrice = Double.valueOf(txfGasPriceTradingTab.getText());
        updateFee(gasPrice);

    }

    @FXML
    void sendToMainWallet(ActionEvent event) {
        String toAddress = txfMainAddressTradingTab.getText();
        String password = pwfMainAddressPasswordTradingTab.getText();
        String filePath = "";
        float value = 0;
        trader.sendETH(password, filePath, toAddress, 2.0, this);
    }

    @FXML
    void sendToAllWallet(ActionEvent event) {

        if (!isValidInfo()) {
            showAlert(false, "MissingInfo", "Main address, filepath, password field is missing or couldn't get data from main wallet");
            return;
        }

        sendToAllWallets();

    }

    /** CHECKING TAB */

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

            // Reset props

            isChecking = true;
            isStopped = false;
            countWallet = 0;
            data.clear();
            tbvDetails.setItems(data);

            //  UI

            btnCheck.setText("Dừng");
            lblCountedWallet.setText(countWallet + "/" + addressList.size());
            lblSum.setText(String.valueOf(sum));

            // Request data

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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String formatNumber(Double amount) {

        BigDecimal bg = new BigDecimal(String.valueOf(amount));
//        Formatter fmt = new Formatter();
//        fmt.format("%" + bg.scale() + "d", bg);

        return String.valueOf(bg);

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
                    showAlert(false, "File type warning!!!", "Only handle excel, word or text file");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showAlert(boolean isError, @Nullable String title, String msg) {

        Platform.runLater(new Runnable() {
            @Override public void run() {
                Alert alert = new Alert(isError ? Alert.AlertType.ERROR : Alert.AlertType.WARNING);
                //alert.setTitle("File type warning!!!");
                alert.setHeaderText(title != null ? "Warning!!!" : title);
                alert.setContentText(msg);

                alert.showAndWait();
            }
        });

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

        // Reset props

        isChecking = false;
        isStopped = true;
        countWallet = 0;
        showedWallets = 0;
        sum = 0d;
        walletList.clear();
        addressList.clear();
        symbolLists.clear();
        data.clear();

        // Reset UI

        btnCheck.setText("Bắt đầu");
        //txfFilePath.setText("");
        lblCountedWallet.setText("");
        lblSum.setText("");
        tbvMain.getItems().clear();
        tbvDetails.getItems().clear();

    }

    private int existedSymbol(String symbol) {

        for (int i = 0; i < symbolLists.size(); i++) {
            if (symbolLists.get(i).getSymbolName().equals(symbol)) {
                return i;
            }
        }

        return -1;

    }

    private void setUpData(WalletETHplorer wallet, Token token, int symbolIndex) {

        // Set up props

        token.setWalletAddress(wallet.getAddress());
        SymbolList symbolList = new SymbolList();
        symbolList.setSymbolName(token.getTokenInfo().getSymbol());
        symbolList.setTokenName(token.getTokenInfo().getName());
        symbolList.setTotal(token.getBalance());
        symbolList.getTokenList().add(token);

        this.symbolLists.add(symbolList);

    }

    private void updateDetailsTable(WalletETHplorer wallet) {

        WalletScan walletScan = new WalletScan();

        if (wallet == null) {
            walletScan.setAccount("Error!");
        } else {
            walletScan.setSerial(countWallet);
            walletScan.setAccount(wallet.getAddress().isEmpty() ? "Không thể lấy địa chỉ" : wallet.getAddress());
            walletScan.setTokenAmount(String.valueOf(wallet.getTokens() != null ? wallet.getTokens().size() : 0));
            walletScan.setBalance(wallet.getETH().getBalance().toString());
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

        sum += Double.parseDouble(walletScan.getBalance());

        walletList.add(walletScan);
        data.add(walletScan);

        // Update UI
        tbvDetails.setItems(data);
        updateCountingLabel();

    }

    private void updateMainTable(WalletETHplorer wallet) {

        if (wallet.getTokens() != null) {

            // Check symbol

            if (symbolLists.size() == 0) {

                for (Token token : wallet.getTokens()) {
                    setUpData(wallet, token, 0);
                }

            } else {

                // Check if symbol has been existed in the list before adding to the list

                int symbolIndex = -1;

                for (Token token : wallet.getTokens()) {

                    symbolIndex = existedSymbol(token.getTokenInfo().getSymbol());

                    if (symbolIndex == -1)
                        setUpData(wallet, token, symbolIndex);
                    else {
                        // Add all token address to the list
                        token.setWalletAddress(wallet.getAddress());
                        symbolLists.get(symbolIndex).getTokenList().add(token);
                    }

                }
            }

            // Prepare main table data

            int count = 0;
            dataResults.clear();
            tbvMain.getItems().clear();
            MainTableModel mainTableModel;

            mainTableModel = new MainTableModel();
            mainTableModel.setSerial(count);
            mainTableModel.setSymbol("All");
            mainTableModel.setTokenName("");
            mainTableModel.setSum(0.0);
            mainTableModel.setNumberOfToken(symbolLists.size());
            mainTableModel.setNumberOfWallet(addressList.size());

            dataResults.add(mainTableModel);
            count += 1;

            for (SymbolList symbolList : symbolLists) {

                mainTableModel = new MainTableModel();
                mainTableModel.setSerial(count);
                mainTableModel.setSymbol(symbolList.getSymbolName());
                mainTableModel.setTokenName(symbolList.getTokenName());
                BigDecimal bg = new BigDecimal(String.valueOf(symbolList.getTotal()));
                Formatter fmt = new Formatter();
                fmt.format("%" + bg.scale() + "f", bg);
                mainTableModel.setSum(Double.valueOf(fmt.toString()));
                mainTableModel.setNumberOfWallet(symbolList.getTokenList().size());

                dataResults.add(mainTableModel);
                count += 1;

            }

            // Update UI

            tbvMain.setItems(dataResults);

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

    private void updateCountingLabel() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblCountedWallet.setText(countWallet + "/" + addressList.size());
                BigDecimal bg = new BigDecimal(String.valueOf(sum));
                Formatter fmt = new Formatter();
                fmt.format("%" + bg.scale() + "f", bg);
                lblSum.setText(fmt.toString());
            }
        });
    }

    /** TRADING TAB */

    private void sendToAllWallets() {
//        String toAddress = "0x8bf3202fAf3bB46460353FC7C8b8494fb4de5BE6";
        String password = pwfMainAddressPasswordTradingTab.getText();
        String filePath = txfMainWalletFilePathTradingTab.getText();

        for (String toAddress :
                addressList) {

            Double value = (mainWallet.getETH().getBalance() - fee);
            trader.sendETH(password, filePath, toAddress, value, this);

        }

    }

    private boolean isValidInfo() {

        if (txfMainAddressTradingTab.getText().isEmpty() ||
                txfMainWalletFilePathTradingTab.getText().isEmpty() ||
                pwfMainAddressPasswordTradingTab.getText().isEmpty() || mainWallet == null) {
            return false;
        }

        return true;

    }

    private void updateFee(Double gas) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txfGasPriceTradingTab.setText(String.valueOf(gas));

                fee = gas * gasLimit / 1000000000;
                lblFeePerWalletTradingTab.setText(formatNumber((double) fee));
                lblTotalFeeTradingTab.setText(formatNumber((double) (fee * totalWallet)));
            }
        });

    }

    /** ----- API SERVICES ----- */

    private void requestSingleWalletInfo(String address) {

        //LoadingAlert.getInstance().show();
        chekingSingleWallet = true;
        WalletETHplorer.getWalletInfo(address, Controller.this);

    }

    private void callAPIGetWalletInfo() {

        //LoadingAlert.getInstance().show();
        // Send request immediately if list is empty

        if (countWallet == 0) {

            // Format address list

            String address = addressList.get(countWallet);
            countWallet += 1;
            //countWallet += countWallet + 1 < addressList.size() ? 1 : addressList.size() - countWallet;

            // Call API
            WalletETHplorer.getWalletInfo(address, Controller.this);

            return;

        }

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                // If reach the last wallet then cancel request

                if (countWallet < addressList.size()) {

                    // Format address list

                    String address = addressList.get(countWallet);
                    countWallet += 1;
                    //countWallet += countWallet + 1 < addressList.size() ? 1 : addressList.size() - countWallet;

                    // Call API
                    WalletETHplorer.getWalletInfo(address, Controller.this);

                }

            }
        }, delayTime);

    }

    public void requestGasData() {
        //LoadingAlert.getInstance().show();
        Gas.getGas(this);
    }

    /** ----- HANDLE RESULTS ----- */

    @Override
    public void walletInfoCallBack(int errorCode, String msg, WalletETHplorer wallet) {

        //LoadingAlert.getInstance().dismiss();

        if (chekingSingleWallet) {
            if (errorCode == SupportKeys.FAILED_CODE) {
                showAlert(true,null, msg);
                return;
            }

            mainWallet = wallet;

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lblBalanceMainWalletTradingTab.setText(formatNumber(wallet.getETH().getBalance()));
                }
            });

            return;
        }

        if (isStopped) {
            System.out.print("Stop plzzz");
            countWallet = showedWallets;
            return;
        }

        if (errorCode == SupportKeys.FAILED_CODE) {
            countWallet -= 1;
            isStopped = true;
            showAlert(true,null, msg);
            callAPIGetWalletInfo();
            return;
        }

        updateDetailsTable(wallet);
        updateMainTable(wallet);
        callAPIGetWalletInfo();

    }

    @Override
    public void requestGasCallBack(int errorCode, String msg, Double gas) {

        //LoadingAlert.getInstance().dismiss();
        if ( errorCode == SupportKeys.FAILED_CODE ) {
            showAlert(true,null, msg);
            return;
        }

        gas += 0.1;
        gasPrice = gas;
        updateFee(gas);

    }

    @Override
    public void sendETHResult(int errorCode, String msg) {

        //LoadingAlert.getInstance().dismiss();
//        if (errorCode == SupportKeys.FAILED_CODE) {
//            showAlert(true, "Send token", msg);
//            return;
//        }

//        showAlert(false, "Send token", msg);
        System.out.print(msg);

    }

}
