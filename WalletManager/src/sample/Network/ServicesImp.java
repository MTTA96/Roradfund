package sample.Network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.*;
import sample.Model.Wallet;
import sample.Util.ServerUrl;

/**
 * Created by zzzzz on 5/22/2018.
 */

public interface ServicesImp {

    /** Get wallet  */

    /** Get subject list */
    @GET(ServerUrl.GET_BALANCE_BY_ADDRESS_URL + "{address}")
    Call<Wallet> getBalanceByAddress(@Path(value="address", encoded=true) String address, @Query("apiKey") String apiKey);

    /** Create course */
//    @POST(ServerUrl.CREATE_COURSE_URL)
//    @FormUrlEncoded
//    Call<BaseResponse> createCourse(@Field("mydata") String myData);

}
