package com.list.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.list.task.activity.BaseActivity;
import com.list.task.activity.EditItemDetailActivity;
import com.list.task.adapter.ItemRvAdapter;
import com.list.task.model.UserItem;
import com.list.task.util.Blog;
import com.list.task.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Main activity of application. Shows list of items and allows user to check them off or select item
 * to edit/delete
 *
 * @author aleksandrTmk
 */
public class MainActivity extends BaseActivity implements ItemClickInterface, View.OnClickListener{
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fab) FloatingActionButton addItemButton;

    private RealmResults<UserItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Blog.d(MainActivity.class, "onCreate()");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        itemList = getAllRealmItems();
        setupViews();
    }

    @Override
    public void onClick(View v) {
        Blog.d(MainActivity.class, "User attempt to create new item");
        openEditScreen(Constants.INTENT_ACTION_ITEM_CREATE, Constants.INTENT_ACTION_ITEM_EDIT_POS_DEFAULT);
    }

    /**
     * Implement ShoppingItemInterface to handle click events on a shopping item and send user
     * to EditDetailItemActivity for editing
     *
     * @param pos Item position
     */
    @Override
    public void onItemClicked(int pos) {
        Blog.d(MainActivity.class, "Item: " + pos + " clicked");
        openEditScreen(Constants.INTENT_ACTION_ITEM_EDIT, pos);
    }

    /**
     * Implement ItemClickInterface when an item is checked. We need to update the information in
     * the local DB
     *
     * @param userItem Item to update
     * @param isChecked Whether or not the item is checked
     */
    @Override
    public void onItemChecked(UserItem userItem, boolean isChecked) {
        UserItem updatedItem = new UserItem();
        updatedItem.setName(updatedItem.getName());
        updatedItem.setQuantity(updatedItem.getQuantity());
        updatedItem.setDescription(updatedItem.getDescription());
        updatedItem.setChecked(isChecked);
        updatetemInRealm(userItem, updatedItem); // save new checked state in DB
    }

    private void setupViews(){
        addItemButton.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        ItemRvAdapter rvAdapter = new ItemRvAdapter(itemList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rvAdapter);
    }

    /**
     * Opens the EditDetailItemActivity for the intentAction
     *
     * @see Constants#INTENT_ACTION_ITEM_CREATE
     * @see Constants#INTENT_ACTION_ITEM_EDIT
     * @param intentAction
     */
    private void openEditScreen(String intentAction, int position){
        if (intentAction == null || intentAction.isEmpty()){
            return;
        }
        Intent detailActivityIntent = new Intent(this, EditItemDetailActivity.class);
        detailActivityIntent.putExtra(Constants.INTENT_EDIT_DETAIL_ACTIVITY, intentAction);
        /**
         * Send over the item position if we are editing an item because item can be deleted, and for
         * that we need position info
         */
        if (intentAction.equals(Constants.INTENT_ACTION_ITEM_EDIT)){
            detailActivityIntent.putExtra(Constants.INTENT_ACTION_ITEM_EDIT_POS, position);
        }
        Blog.d(MainActivity.class, "Send user to edit item detail screen");
        startActivity(detailActivityIntent);
        finish();
    }
}
