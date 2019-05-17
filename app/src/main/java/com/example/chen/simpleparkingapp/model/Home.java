package com.example.chen.simpleparkingapp.model;

import java.util.List;

public class Home {
    private List<Parking> parkings;

    private List<Banner> banners;

    public List<Parking> getParkings() {
        return parkings;
    }

    public void setParkings(List<Parking> parkings) {
        this.parkings = parkings;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }
}
