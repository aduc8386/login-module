package omt.aduc8386.loginmodule.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


public class User extends RealmObject {

    @SerializedName("id")
    private Integer id;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public String getAvatar() {
        return avatar != null ? avatar : "https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_1280.png";
    }

    public String getFirstName() {
        return firstName != null ? firstName : "";
    }
}
