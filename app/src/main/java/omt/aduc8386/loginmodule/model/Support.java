package omt.aduc8386.loginmodule.model;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Support extends RealmObject {

    @SerializedName("url")
    private String url;
    @SerializedName("text")
    private String text;
}
