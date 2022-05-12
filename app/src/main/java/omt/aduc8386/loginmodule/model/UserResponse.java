package omt.aduc8386.loginmodule.model;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class UserResponse extends RealmObject {

    private Integer page;
    private Support support;
    private Integer total;

    @SerializedName("per_page")
    private Integer perPage;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("data")
    private RealmList<User> users;


    public Integer getPage() {
        return page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Support getSupport() {
        return support;
    }

    public RealmList<User> getUsers() {
        return users;
    }
}
