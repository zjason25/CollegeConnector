package edu.uci.ics.fabflixmobile.data.model;

/**
 * Movie class that captures movie information for movies retrieved from MovieListActivity
 */
public class School {
    private final String name;
    private final String rating;
    private final String location;
    private final String website;
    private final String genre;
    private final String telephone;


    public School(String name, String rating, String location,  String website, String genre, String telephone) {
        this.name = name;
        this.rating = rating;
        this.location = location;
        this.website = website;
        this.genre = genre;
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }
    public String getRating() {
        return rating;
    }
    public String getLocation() {return location;}
    public  String getWebsite() {return website;}
    public String getGenre() {return genre;}
    public String getTelephone() {return telephone;}
}