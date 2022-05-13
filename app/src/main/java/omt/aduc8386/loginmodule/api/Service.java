package omt.aduc8386.loginmodule.api;

import omt.aduc8386.loginmodule.model.Account;
import omt.aduc8386.loginmodule.model.MyResponse;
import omt.aduc8386.loginmodule.model.User;
import omt.aduc8386.loginmodule.model.UserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    @POST("login")
    Call<MyResponse> login(@Body Account account);

    @POST("users")
    Call<User> addUser(@Body User user);

    @GET("users")
    Call<UserResponse> getUsers(@Query("page") int pageNumber);

}
