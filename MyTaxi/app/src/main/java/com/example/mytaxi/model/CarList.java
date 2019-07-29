package com.example.mytaxi.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CarList {

        @SerializedName("poiList")
        private List<Car> cars = new ArrayList<>();


    public List<Car> getCars() {
        return cars;
    }

}
