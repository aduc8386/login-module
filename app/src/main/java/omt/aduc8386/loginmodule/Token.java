package omt.aduc8386.loginmodule;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("token")
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }
}
