package sample.Util;

/**
 * Created by zzzzz on 10/4/2017.
 *
 * Description: Lưu các link api của server.
 */

public class ServerUrl {
    //Link server
//    public static final String ServerUrl = "http://api.ethplorer.io/";
//    public static final String GET_BALANCE_BY_ADDRESS_URL = ServerUrl + "getAddressInfo/";

    // EtherScan
    public static final String ServerUrl = "https://api.etherscan.io/";
    public static final String GET_BALANCE_BY_ADDRESS_URL = ServerUrl + "api/";

    /** ----- ETH PLORER ----- */

    public static final String EthplorerUrl = "http://api.ethplorer.io/";
    public static final String GET_ADDRESS_INFO = EthplorerUrl + "getAddressInfo/";

    public static final String GAS_BASE_URL = "https://www.etherchain.org/api/";
    public static final String GET_GAS_URL = GAS_BASE_URL + "gasPriceOracle";

}
