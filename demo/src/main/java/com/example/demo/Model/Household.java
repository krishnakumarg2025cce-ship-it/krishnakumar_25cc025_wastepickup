package com.example.demo.model;

public class Household {

    private int id;
    private String name;
    private String zone;
    private int segregationScore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public int getSegregationScore() {
        return segregationScore;
    }

    public void setSegregationScore(int segregationScore) {
        this.segregationScore = segregationScore;
    }
}