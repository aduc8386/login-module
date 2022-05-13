package omt.aduc8386.loginmodule.model;

import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    private String name;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
