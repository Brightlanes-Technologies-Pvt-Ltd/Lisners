package com.lisners.counsellor.ApiModal;

public class SpacializationMedel {
    int id , active;
    String title = "P" , image , description , link ;
    PivotModel pivot ;
    boolean check =false ;

    public SpacializationMedel(int id, String title) {
        this.id = id;
        this.title = title;
        this.image = image;
    }
    public boolean isCheck()
    {
        return check ;
    }

    public void setCheck(boolean check)
    {
        this.check = check ;
    }

    public int getId() {
        return id;
    }

    public int getActive() {
        return active;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}
