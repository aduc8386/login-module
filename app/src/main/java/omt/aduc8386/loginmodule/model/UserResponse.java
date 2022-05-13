package omt.aduc8386.loginmodule.model;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class UserResponse extends RealmObject {

    @SerializedName("page")
    private Integer page;
    @SerializedName("support")
    private Support support;
    @SerializedName("total")
    private Integer total;
    @SerializedName("per_page")
    private Integer perPage;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("data")
    private RealmList<User> users;

    public RealmList<User> getUsers() {
        return users;
    }
}
