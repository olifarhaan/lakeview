package com.olifarhaan.domains;

public enum AddOn {

    BREAKFAST(200),
    LUNCH(300),
    DINNER(400),
    AIRPORT_SHUTTLE(200),
    SPA_ACCESS(300),
    GYM_ACCESS(100),
    LATE_CHECKOUT(100),
    ROOM_SERVICE(150),
    LAUNDRY_SERVICE(200),
    MINIBAR_ACCESS(50),
    CITY_TOUR(250),
    BABYSITTING_SERVICE(300);

    private final double price;

    AddOn(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

}
