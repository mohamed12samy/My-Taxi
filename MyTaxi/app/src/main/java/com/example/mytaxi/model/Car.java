package com.example.mytaxi.model;

import androidx.lifecycle.LiveData;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Car {

    @SerializedName("id")
    private int Id;
    @SerializedName("fleetType")
    private String type;
    @SerializedName("heading")
    private double heading;
    @SerializedName("coordinate")
    private Coordinate coordinates = new Coordinate();

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public Coordinate getCordinates() {
        return coordinates;
    }

    public void setCordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public class Coordinate{
        @SerializedName("latitude")
        private double latitude;
        @SerializedName("longitude")
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}