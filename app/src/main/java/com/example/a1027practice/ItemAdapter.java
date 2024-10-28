package com.example.a1027practice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// For grouping, we'll need to have a header view for each listId
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ListItem> items;
    private Map<String, List<Item>> headerItemMap;

    public ItemAdapter(List<ListItem> items) {
        this.items = items;
        this.headerItemMap = new HashMap<>();
    }

    public void setHeaderItemMap(Map<Integer, List<Item>> validItemsMap, Map<Integer, List<Item>> invalidItemsMap) {
        headerItemMap = new HashMap<>();

        // Add valid items
        for (Map.Entry<Integer, List<Item>> entry : validItemsMap.entrySet()) {
            String key = entry.getKey() + "_valid";
            headerItemMap.put(key, entry.getValue());
        }

        // Add invalid items
        for (Map.Entry<Integer, List<Item>> entry : invalidItemsMap.entrySet()) {
            String key = entry.getKey() + "_invalid";
            headerItemMap.put(key, entry.getValue());
        }
    }

    // ViewHolder for Item
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView idTextView;
        public TextView nameTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.itemId);
            nameTextView = itemView.findViewById(R.id.itemName);
        }
    }

    // ViewHolder for Header
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView listIdTextView;
        public ImageView arrowIcon;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            listIdTextView = itemView.findViewById(R.id.listId);
            arrowIcon = itemView.findViewById(R.id.arrowIcon);

            // Set up click listener for header
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    HeaderItem header = (HeaderItem) items.get(position);
                    toggleSection(header, position);
                }
            });
        }
    }

    private void toggleSection(HeaderItem header, int position) {
        int nextPosition = position + 1;
        if (header.isExpanded()) {
            // Collapse the section
            header.setExpanded(false);

            int count = 0;
            while (nextPosition < items.size() && items.get(nextPosition).getType() == ListItem.TYPE_ITEM) {
                items.remove(nextPosition);
                count++;
            }

            notifyItemRangeRemoved(nextPosition, count);
            notifyItemChanged(position);
        } else {
            // Expand the section
            header.setExpanded(true);

            String key = header.getListId() + (header.isValidSection() ? "_valid" : "_invalid");
            List<Item> itemList = headerItemMap.get(key);
            if (itemList != null) {
                items.addAll(nextPosition, itemList);
                notifyItemRangeInserted(nextPosition, itemList.size());
                notifyItemChanged(position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == ListItem.TYPE_HEADER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header_layout, viewGroup, false);
            return new HeaderViewHolder(view);
        } else { // TYPE_ITEM
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ListItem listItem = items.get(position);

        if (viewHolder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            HeaderItem headerItem = (HeaderItem) listItem;

            String sectionType = headerItem.isValidSection() ? "Valid Items" : "Invalid Items";
            headerViewHolder.listIdTextView.setText("List ID: " + headerItem.getListId() + " - " + sectionType);

            // Rotate the arrow icon based on expanded/collapsed state
            if (headerItem.isExpanded()) {
                headerViewHolder.arrowIcon.setRotation(0); // Pointing down
            } else {
                headerViewHolder.arrowIcon.setRotation(-90); // Pointing right
            }
        } else if (viewHolder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            Item item = (Item) listItem;
            itemViewHolder.idTextView.setText("ID: " + item.getId());
            itemViewHolder.nameTextView.setText("Name: " + item.getName());
        }
    }
}
