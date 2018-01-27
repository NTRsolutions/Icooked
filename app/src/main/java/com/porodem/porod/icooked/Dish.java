package com.porodem.porod.icooked;

/**
 * Created by porod on 24.08.2015.
 */
public class Dish {

    int dishID;
    String title;
    String sound;
    int img;
    int cookedTime;
    String ingrList;
    String recipe;

    public void Dish(int dishID, String title, String sound, int img, int cookedTime, String ingrList, String recipe) {
        this.dishID = dishID;
        this.title = title;
        this.sound = sound;
        this.img = img;
        this.cookedTime = cookedTime;
        this.ingrList = ingrList;
        this.recipe = recipe;
    }


}
