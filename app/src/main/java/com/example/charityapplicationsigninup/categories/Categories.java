package com.example.charityapplicationsigninup.categories;

/**
 * This class contains the category object and its specs
 */
public class Categories {
    //title of the category
    private String title;
    //description of the category
    private String description;
    //Url of the background of the category
    private String backgroundUrl;
    //ID of the category used to classify problems into categories
    private int categoryId;

    public Categories() {
        //for firebase
    }

    public Categories(String title, String description, String backgroundUrl, int categoryId) {
        this.title = title;
        this.description = description;
        this.backgroundUrl = backgroundUrl;
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
