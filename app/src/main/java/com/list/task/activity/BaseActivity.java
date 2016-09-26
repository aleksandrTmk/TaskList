package com.list.task.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.list.task.model.UserItem;
import com.list.task.util.Blog;
import com.list.task.util.Constants;
import com.list.task.R;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Base Activity that provides access to Realm DB
 *
 * @author aleksandrTmk
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRealmDB();
        if (!didAppRunBefore()){
            handleFirstAppRun();
        }
    }

    //region DB methods
    /**
     * Adds the item to realm DB for persistent storage
     *
     * @param item
     */
    public void addItemToRealm(UserItem item){
        if (item == null){
            Blog.e(BaseActivity.class, "Attempt to add null item to DB");
            return;
        }
        if (realm == null){
            setupRealmDB();
        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(item);
        realm.commitTransaction();
    }


    /**
     * Updates original item in realm DB with fields from updated item
     *
     * Note: Realm requires all object updates to happen inside a transaction, hence why we have to pass
     * all the updated object into the method.
     *
     * @param originalItem
     * @param updateItem
     */
    public void updatetemInRealm(UserItem originalItem, UserItem updateItem){
        if (originalItem == null || updateItem == null){
            Blog.e(BaseActivity.class, "Attempt to update null item or updated values are null");
            return;
        }
        if (realm == null){
           setupRealmDB();
        }
        realm.beginTransaction();
        if (updateItem.getName() != null) originalItem.setName(updateItem.getName());
        if (updateItem.getDescription() != null) originalItem.setDescription(updateItem.getDescription());
        if (updateItem.getQuantity() != null) originalItem.setQuantity(updateItem.getQuantity());
        originalItem.setChecked(updateItem.isChecked());

        realm.copyToRealmOrUpdate(originalItem);
        realm.commitTransaction();
        Blog.d(BaseActivity.class, "Item: " + originalItem.getName() + " added to DB");
    }

    /**
     * Deletes an item from realm DB based on the position in results list
     *
     * @param pos
     */
    public void deleteItemFromRealm(final int pos){
        if (realm == null){
            setupRealmDB();
        }
        realm.beginTransaction(); // Realm docs say to get results inside the transaction for sync safety
        RealmResults<UserItem> allResult = getAllRealmItems();
        if (allResult != null){
            allResult.get(pos).deleteFromRealm();
        }
        realm.commitTransaction();
        Blog.d(BaseActivity.class, "Item at: " + pos + " removed from DB");
    }

    /**
     * Gets an item from realm DB based on the position in results list
     *
     * @param pos
     * @return
     */
    public UserItem getItemFromRealm(int pos){
        RealmResults<UserItem> allResult = getAllRealmItems();
        if (allResult == null){
            Blog.e(BaseActivity.class, "Failed to get item at position: " + pos + " from DB");
            return null;
        }
        Blog.d(BaseActivity.class, "Item at: " + pos + " returned from DB");
        return allResult.get(pos);
    }

    /**
     * Returns all shopping items stored in the DB
     * @return
     */
    public RealmResults<UserItem> getAllRealmItems(){
        if (realm == null){
            setupRealmDB();
        }
        // TODO maybe sort the items on timestamp of creation?
        Blog.d(BaseActivity.class, "Returning all DB items");
        return realm.where(UserItem.class).findAll();
    }
    //endregion


    //region First App run methods
    /**
     * Check if this application has ran before. If it has, the Constants#PREF_APP_RAN_BEFORE flag will
     * be true, otherwise we return false.
     *
     * @return True if app ran before, false otherwise
     */
    private boolean didAppRunBefore(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs == null){
            return false;
        }
        return prefs.getBoolean(Constants.PREF_APP_RAN_BEFORE, false);
    }

    private void handleFirstAppRun(){
        Blog.d(BaseActivity.class, "App running first time!");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        if (editor != null ){
            editor.putBoolean(Constants.PREF_APP_RAN_BEFORE, true);
            editor.apply();
        }
        addSampleItemToDB();
    }

    /**
     * Creates a sample list item as an example for the user and adds it to DB. This only happens the first time the
     * application runs.
     */
    private void addSampleItemToDB(){
        Blog.d(BaseActivity.class, "Create sample item");
        UserItem sampleItem = new UserItem();
        sampleItem.setName(getString(R.string.sample_item_name));
        sampleItem.setDescription(getString(R.string.sample_item_description));
        sampleItem.setQuantity(getString(R.string.sample_item_quantity));
        sampleItem.setChecked(true);
        addItemToRealm(sampleItem);
    }

    private void setupRealmDB(){
        if (realm == null){
            realm = Realm.getDefaultInstance();
        }
    }
    //endregion
}
