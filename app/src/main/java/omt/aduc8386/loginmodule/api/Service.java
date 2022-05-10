package omt.aduc8386.loginmodule.api;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import omt.aduc8386.loginmodule.Account;
import omt.aduc8386.loginmodule.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Service {

    @POST("api/login")
    Call<Token> login(@Body Account account);

}
