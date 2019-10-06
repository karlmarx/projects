package com.example;

import com.google.gson.annotations.SerializedName;

public class Animal {
    @SerializedName('name')
    private String name;

    @SerializedName('pic')
    private String pic;

    @SerializedName('description')
    private String description;

    public Animal() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Animal(String name, String pic, String description) {
        this.name = name;
        this.pic = pic;
        this.description = description;
    }
}
