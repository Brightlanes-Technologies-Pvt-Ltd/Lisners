package com.lisners.counsellor.ApiModal;

public class ModellistItem {
    boolean selected = false;
    String id, name;


    public ModellistItem(String id, String name) {
        this.selected = false;
        this.id = id;
        this.name = name;
    }

    public int getIntId() {
        if (id != null)
            return Integer.valueOf(id);
        else
            return 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

