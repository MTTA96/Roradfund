package sample.Model.TableModels;

import java.math.BigDecimal;

public class TradingProgressTableModel {

    private int serial;
    private String adress;
    private BigDecimal sendingValue;
    private String status;

    public TradingProgressTableModel(int serial, String adress, BigDecimal sendingValue, String status) {
        this.serial = serial;
        this.adress = adress;
        this.sendingValue = sendingValue;
        this.status = status;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public BigDecimal getSendingValue() {
        return sendingValue;
    }

    public void setSendingValue(BigDecimal sendingValue) {
        this.sendingValue = sendingValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
