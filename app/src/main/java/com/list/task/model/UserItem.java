package com.list.task.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Model used to represent an item user creates with a name, description and quantity, whether it is
 * checked, etc.
 *
 * @author aleksandrTmk
 */
public class UserItem extends RealmObject implements Serializable {

    @PrimaryKey
    private String name;
    private String description;
    private String quantity;
    private boolean checked;

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean mChecked) {
        this.checked = mChecked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String mDescription) {
        this.description = mDescription;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String mQuantity) {
        this.quantity = mQuantity;
    }
}
