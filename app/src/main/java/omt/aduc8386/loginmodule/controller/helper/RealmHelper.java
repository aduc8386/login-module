package omt.aduc8386.loginmodule.controller.helper;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import omt.aduc8386.loginmodule.model.User;

public class RealmHelper {

    private static Realm instance;

    public static final String REALM = "REALM";

    private static void init() {
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
        init();
        return instance;
    }

    public static void insertOrUpdateUsersToRealm(List<User> users) {
        try{
            getInstance().executeTransaction(transactionRealm -> {
                transactionRealm.insertOrUpdate(users);
                transactionRealm.commitTransaction();
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertOrUpdateUserToRealm(User user) {
        try{
            getInstance().executeTransaction(transactionRealm -> {
                transactionRealm.insertOrUpdate(user);
                transactionRealm.commitTransaction();
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
