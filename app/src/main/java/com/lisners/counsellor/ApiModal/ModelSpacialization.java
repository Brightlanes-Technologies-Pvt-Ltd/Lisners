package com.lisners.counsellor.ApiModal;

public class ModelSpacialization {
    int id ;
    String   name ,title ;
    boolean check =false ;

    public ModelSpacialization(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ModelSpacialization(int id, String name, boolean check) {
        this.id = id;
        this.name = name;
        this.check = check ;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        if(name!=null)
        return name;
        else return title ;
    }

    public boolean isCheck() {
        return check;
    }
}

