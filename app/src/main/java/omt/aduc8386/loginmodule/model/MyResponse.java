package omt.aduc8386.loginmodule.model;

import com.google.gson.annotations.SerializedName;

public class MyResponse {

    @SerializedName("token")
    private String authToken;
    @SerializedName("error")
    private String error;

    public String getAuthToken() {
        return authToken;
    }
}
