package omt.aduc8386.loginmodule.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class User extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private Integer id;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName(value = "firstName", alternate = {"first_name", "name"})
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("job")
    private String job;
    @SerializedName("createdAt")
    private String createdAt;

    public User() {
    }

    public User(String firstName, String job) {
        this.firstName = firstName;
        this.job = job;
    }

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    public String getEmail() {
        return email != null ? email : "abc@example.com";
    }

    public String getAvatar() {
        return avatar != null ? avatar : "https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_1280.png";
    }

    public String getFirstName() {
        return firstName != null ? firstName : "";
    }
}
