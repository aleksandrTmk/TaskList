package com.list.task.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.list.task.ItemClickInterface;
import com.list.task.R;
import com.list.task.model.UserItem;
import com.list.task.util.Blog;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * RecyclerView adapter the items user will create
 *
 * Note: Interface is kept static so re-using this adapter will not work correctly
 *
 * @author aleksandrTmk
 */
public class ItemRvAdapter extends RecyclerView.Adapter<ItemRvAdapter.ListItemViewHolder> {
    private ItemClickInterface itemClickInterface;
    private RealmResults<UserItem> dataList;

    public ItemRvAdapter(RealmResults<UserItem> dataList, ItemClickInterface clickInterface){
        this.dataList = dataList;;
        itemClickInterface = clickInterface;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent == null || parent.getContext() == null){
            return null;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder listItemViewHolder, int position) {
        Blog.d(ItemRvAdapter.class, "onBindViewHolder()");
        if (listItemViewHolder == null || dataList == null){
            return;
        }
        UserItem userItem = dataList.get(position);
        if (userItem == null){
            return;
        }
        listItemViewHolder.nameTextView.setText(userItem.getName());
        listItemViewHolder.quantityTextView.setText(userItem.getQuantity());
        listItemViewHolder.checkBox.setChecked(userItem.isChecked());

        String description = userItem.getDescription();
        if (description == null || description.isEmpty()){
            listItemViewHolder.descriptionTextView.setVisibility(View.GONE);
        } else {
            listItemViewHolder.descriptionTextView.setVisibility(View.VISIBLE);
            listItemViewHolder.descriptionTextView.setText(description);
        }
    }

    @Override
    public int getItemCount() {
        if (dataList == null){
            return 0;
        }
        return dataList.size();
    }

    /**
     * ViewHolder class that holds the views of each list item
     */
    public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        @BindView(R.id.list_item_name) TextView nameTextView;
        @BindView(R.id.list_item_quantity) TextView quantityTextView;
        @BindView(R.id.list_item_description) TextView descriptionTextView;
        @BindView(R.id.list_item_checkbox) CheckBox checkBox;

        public ListItemViewHolder(View container){
            super(container);
            ButterKnife.bind(this, container);

            container.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            Blog.d(ItemRvAdapter.class, "Clicked on view at pos: " + getLayoutPosition());
            itemClickInterface.onItemClicked(getLayoutPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (dataList == null || itemClickInterface == null){
                return;
            }
            UserItem checkedItem = dataList.get(getLayoutPosition());
            if (checkedItem.isChecked() == isChecked){
                Blog.d(ItemRvAdapter.class, "Item check state changed to same value, do nothing");
                return;
            }
            Blog.d(ItemRvAdapter.class, "Item is checked: " + isChecked);
            checkBox.setChecked(isChecked);
            itemClickInterface.onItemChecked(checkedItem, isChecked);
        }
    }
}
