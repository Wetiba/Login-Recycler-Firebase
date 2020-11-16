package com.example.onlineshopping;

public class Item {
    private String name;
    private String imageURI;
    private String key;
    private String description;
    private  int position;

    public Item (){

    }

    public Item(String name, String imageURI, String key,String description, int position ) {
        if (name.trim().equals("")){
            name = "No Name";
        }

        this.name = name;
        this.imageURI = imageURI;
        this.key = key;
        this.description = description;
        this.position = position;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
