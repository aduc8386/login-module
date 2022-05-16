package omt.aduc8386.loginmodule.helper;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import omt.aduc8386.loginmodule.model.User;

public class RealmHelper {

    private static Realm instance;

    public static final String REALM = "REALM";

    public static void init() {
        if (instance == null) {
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name(REALM)
                    .allowQueriesOnUiThread(true)
                    .allowWritesOnUiThread(true)
                    .build();
            instance = Realm.getInstance(config);
        }
    }

    public static Realm getInstance() {
        return instance;
    }

    public static void insertToRealm(User user) {
        try{
            instance.executeTransaction(transactionRealm -> {
                transactionRealm.insertOrUpdate(user);
                transactionRealm.commitTransaction();
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
