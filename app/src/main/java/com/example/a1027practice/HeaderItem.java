package com.example.a1027practice;

public class HeaderItem implements ListItem {
    private int listId;
    private boolean isExpanded;
    private boolean isValidSection; // True for valid items, false for invalid items

    public HeaderItem(int listId, boolean isValidSection) {
        this.listId = listId;
        this.isValidSection = isValidSection;
        this.isExpanded = true; // Default to expanded
    }

    public int getListId() {
        return listId;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isValidSection() {
        return isValidSection;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_HEADER;
    }
}
