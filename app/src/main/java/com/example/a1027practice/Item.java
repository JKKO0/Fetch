package com.example.a1027practice;

public class Item implements ListItem {
    private int id;
    private int listId;
    private String name;

    public Item(int id, int listId, String name) {
        this.id = id;
        this.listId = listId;
        this.name = name;
    }

    public int getId() { return id; }

    public int getListId() { return listId; }

    public String getName() { return name; }

    @Override
    public int getType() {
        return ListItem.TYPE_ITEM;
    }
}
