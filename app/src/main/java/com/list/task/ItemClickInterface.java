package com.list.task;

import com.list.task.model.UserItem;

/**
 * Interface used to pass information about the clicked item
 *
 * @author aleksandrTmk
 */
public interface ItemClickInterface {
    void onItemClicked(int pos);
    void onItemChecked(UserItem userItem, boolean isChecked);
}
