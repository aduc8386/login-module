package omt.aduc8386.loginmodule;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import omt.aduc8386.loginmodule.model.User;

public class RealmHelper {

    private Realm instance;

    public static final String REALM = "REALM";

    public RealmHelper() {
        if (instance == null) {
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name(REALM)
                    .allowQueriesOnUiThread(true)
                    .allowWritesOnUiThread(true)
                    .build();
            instance = Realm.getInstance(config);
        }
    }

    public Realm getInstance() {
        return instance;
    }

    public void insertToRealm(User user) {
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
