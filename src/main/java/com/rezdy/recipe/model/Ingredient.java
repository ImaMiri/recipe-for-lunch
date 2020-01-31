package com.rezdy.recipe.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    private String title;
    @SerializedName("best-before")
    private String bestBefore;
    @SerializedName("use-by")
    private String useBy;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(String bestBefore) {
        this.bestBefore = bestBefore;
    }

    public String getUseBy() {
        return useBy;
    }

    public void setUseBy(String useBy) {
        this.useBy = useBy;
    }
}
