package com.lisners.counsellor.ApiModal;

public class PageModel {
    int id ;
    String title ,
            slug ,
            page_content,
            page_link ;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getPage_link() {
        return page_link;
    }

    public String getPage_content() {
        return page_content;
    }
}
