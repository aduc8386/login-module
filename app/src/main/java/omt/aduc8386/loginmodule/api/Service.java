package omt.aduc8386.loginmodule.api;

import omt.aduc8386.loginmodule.model.Account;
import omt.aduc8386.loginmodule.model.MyResponse;
import omt.aduc8386.loginmodule.model.UserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Service {

    @POST("api/login")
    Call<MyResponse> login(@Body Account account);

    @GET("api/users")
    Call<UserResponse> getUsers();

}
