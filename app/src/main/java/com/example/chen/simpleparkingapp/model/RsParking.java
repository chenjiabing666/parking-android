package com.example.chen.simpleparkingapp.model;

import java.util.List;

public class RsParking {
    private List<ParkingImage> images;

    private Parking parking;

    public List<ParkingImage> getImages() {
        return images;
    }

    public void setImages(List<ParkingImage> images) {
        this.images = images;
    }

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }
}
