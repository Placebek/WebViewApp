package com.example.kazplantaimobile;

import java.util.ArrayList;
import java.util.List;

public abstract class Image {
    public Image(String imageId, ArrayList<String> images) {
        this.imageId = imageId;
        this.images = images;
    }

    public String imageId;

    public ArrayList<String> images;

    public void setImageId(String imageId){
        this.imageId = imageId;
    }

    public void setImages(ArrayList<String> images){
        this.images = images;
    }

    public String getImageId(){
        return this.imageId;
    }

    public ArrayList<String> getImages(){
        return this.images;
    }

    public abstract int getFormat();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract long getTimestamp();

    public abstract android.media.Image.Plane[] getPlanes();

    public abstract void close();
}
