package com.list.task.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.list.task.MainActivity;
import com.list.task.R;
import com.list.task.model.UserItem;
import com.list.task.util.Blog;
import com.list.task.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity representing the edit item detail screen where you can update all relevant item fields
 * or delete the item
 *
 * @author aleksandrTmk
 */
public class EditItemDetailActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.edit_item_name) EditText itemNameEditText;
    @BindView(R.id.edit_item_quantity) EditText itemQuantityEditText;
    @BindView(R.id.edit_item_description) EditText itemDescriptionEditText;

    @BindView(R.id.edit_item_save_button) Button saveButton;
    @BindView(R.id.edit_item_delete_button) Button deleteButton;
    @BindView(R.id.edit_item_cancel_button) Button cancelButton;

    private String itemName;
    private String itemQuantity;
    private String itemDescription;
    private String intentAction = null;
    private int itemPosition;
    private UserItem userItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);

        setButtonClickListeners();
        handleIntent();
    }

    /**
     * Users who pressed back and came into this activity to create or edit an item
     * should be sent back to main activity or else where if we expand functionality
     */
    @Override
    public void onBackPressed() {
        if (intentAction == null){
            super.onBackPressed();
            return;
        }
        switch (intentAction){
            case Constants.INTENT_ACTION_ITEM_CREATE:
            case Constants.INTENT_ACTION_ITEM_EDIT:
                sendUserToMainActivity();
                break;
            default:
                Blog.d(EditItemDetailActivity.class, "Back press for: " + intentAction + " not handled");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_item_cancel_button:
                Blog.d(EditItemDetailActivity.class, "onClick cancel");
                sendUserToMainActivity();
                break;
            case R.id.edit_item_delete_button:
                Blog.d(EditItemDetailActivity.class, "onClick delete");
                deleteItemFromRealm(itemPosition);
                sendUserToMainActivity();
                break;
            case R.id.edit_item_save_button:
                Blog.d(EditItemDetailActivity.class, "onClick save");
                saveItemToDB();
                sendUserToMainActivity();
                break;
        }
    }

    /**
     * Sends user back to main activity
     */
    private void sendUserToMainActivity(){
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    /**
     * Grabs user input from edit texts and validates it. If valid, saves the item to realm DB or updates
     * an existing copy.
     */
    private void saveItemToDB(){
        updateItemInfoStrings();
        if (!isValidInput()){
            Blog.i(EditItemDetailActivity.class, "User input not valid. Will not save item at this time");
            return;
        }
        UserItem updatedItem = new UserItem();
        updatedItem.setName(itemName);
        updatedItem.setDescription(itemDescription);
        updatedItem.setQuantity(itemQuantity);
        updatedItem.setChecked(userItem.isChecked());
        updatetemInRealm(userItem, updatedItem);
    }

    /**
     * Handles any incoming intents. This activity is meant to create new items or edit existing ones.
     *
     * @see Constants#INTENT_ACTION_ITEM_CREATE
     * @see Constants#INTENT_ACTION_ITEM_EDIT
     */
    private void handleIntent(){
        if (getIntent() == null) {
            return;
        }
        intentAction = getIntent().getStringExtra(Constants.INTENT_EDIT_DETAIL_ACTIVITY);
        handleIntentAction();
    }

    private void handleIntentAction(){
        if (intentAction == null || intentAction.isEmpty()){
            return;
        }
        toggleViews(intentAction);
        switch (intentAction){
            case Constants.INTENT_ACTION_ITEM_CREATE:
                userItem = new UserItem();
                break;
            case Constants.INTENT_ACTION_ITEM_EDIT:
                itemPosition = getIntent().getIntExtra(Constants.INTENT_ACTION_ITEM_EDIT_POS, Constants.INTENT_ACTION_ITEM_EDIT_POS_DEFAULT);
                if (itemPosition == Constants.INTENT_ACTION_ITEM_EDIT_POS_DEFAULT){
                    Blog.i(EditItemDetailActivity.class, "Attempting to edit item whose position we don't know");
                    return;
                }
                userItem = getItemFromRealm(itemPosition);
                setItemInfo(userItem);
                break;
            default:
                break;
        }
    }

    /**
     * Toggle views for creating an item vs editing an item
     *
     * @param intentAction
     */
    private void toggleViews(String intentAction){
        if (cancelButton == null || deleteButton == null){
            return;
        }
        switch (intentAction){
            case Constants.INTENT_ACTION_ITEM_CREATE:
                cancelButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.GONE);
                break;
            case Constants.INTENT_ACTION_ITEM_EDIT:
                deleteButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * Set click listeners for save, cancel, delete buttons
     */
    private void setButtonClickListeners(){
        deleteButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }


    //region User Input
    /**
     * Sets the item strings from edit texts and does validation on the input
     */
    private void updateItemInfoStrings(){
        itemName = getStringFromEditText(itemNameEditText, getString(R.string.edit_item_error_name_empty));
        itemDescription = getStringFromEditText(itemDescriptionEditText, null);
        itemQuantity = getStringFromEditText(itemQuantityEditText, getString(R.string.edit_item_error_quantity_empty));
    }

    /**
     * Validates that user has input item name and quantity. These are required.
     *
     * @return
     */
    private boolean isValidInput(){
        if (itemDescription == null){
            itemDescription = "";
        }
        if (itemName == null || itemName.isEmpty()){
            return false;
        }
        if (itemQuantity == null || itemQuantity.isEmpty()){
            return false;
        }
        return true;
    }

    /**
     * Returns a string from the editText. If the string is null or empty, we set the error on the editText
     * @param editText
     * @param error
     * @return
     */
    private String getStringFromEditText(EditText editText, String error){
        String editString;
        if (editText == null || editText.getText() == null){
            Blog.e(EditItemDetailActivity.class, "Failed to get string from EditText");
            return null;
        }
        editString = editText.getText().toString();
        if (error != null && editString.isEmpty()){
            editText.setError(error);
            return null;
        }
        return editString;
    }

    private void setEditTextString(EditText editText, String text){
        if (editText == null || text == null){
            return;
        }
        editText.setText(text);
        editText.setSelection(text.length());
    }

    /**
     * Sets the shopping item info into the edit text fields
     */
    private void setItemInfo(UserItem userItem){
        if (userItem == null){
            return;
        }
        setEditTextString(itemNameEditText, userItem.getName());
        setEditTextString(itemQuantityEditText, userItem.getQuantity());
        setEditTextString(itemDescriptionEditText, userItem.getDescription());
    }
    //endregion User Input

}
