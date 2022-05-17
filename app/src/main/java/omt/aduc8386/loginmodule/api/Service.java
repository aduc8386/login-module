package omt.aduc8386.loginmodule.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import omt.aduc8386.loginmodule.model.Account;
import omt.aduc8386.loginmodule.model.MyResponse;
import omt.aduc8386.loginmodule.model.User;
import omt.aduc8386.loginmodule.model.UserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @POST("login")
    Call<MyResponse> login(@Body Account account);

    @POST("users")
    Call<User> addUser(@Body User user);

    @PATCH("users/{id}")
    Call<User> updateUser(@Path("id") int userId, @Body User user);

    @GET("users")
    Call<UserResponse> getUsers(@Query("page") int pageNumber);

    @GET("users")
    Call<UserResponse> getUsersDelayed(@Query("delay") int delayLevel);

    @GET("users/{id}")
    Call<JsonObject> getUserById(@Path("id") int userId);

}
