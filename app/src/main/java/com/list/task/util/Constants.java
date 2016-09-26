package com.list.task.util;

/**
 * Class holding various constants used by the app
 *
 * @author aleksandrTmk
 */
public class Constants {

    //region Intent Constants
    /**
     * Constants used as the key from the <k,v> intent pairs
     */
    public static final String INTENT_EDIT_DETAIL_ACTIVITY = "intent-edit-detail-activity";
    /**
     * Constants depicting actions. Used as the value from the <k,v> intent pairs.
     *
     * User intends to create a new item
     */
    public static final String INTENT_ACTION_ITEM_CREATE = "intent-item-create";
    /**
     * Constants depicting actions. Used as the value from the <k,v> intent pairs.
     *
     * User intends to edit an item
     */
    public static final String INTENT_ACTION_ITEM_EDIT = "intent-item-edit";
    /**
     * Intent constant holding 'extra = position' of the item user intends to edit
     */
    public static final String INTENT_ACTION_ITEM_EDIT_POS = "intent-item-edit-pos";
    /**
     * Default position of an item user intends to edit, set to -1
     */
    public static final int INTENT_ACTION_ITEM_EDIT_POS_DEFAULT = -1;
    //endregion Intent Constants

    /**
     * Key for shared preference flag that tracks if the app has ran before.
     *
     * Value will be true if it has ran before, false otherwise.
     */
    public static final String PREF_APP_RAN_BEFORE = "pref-app-has-run-before";
}
