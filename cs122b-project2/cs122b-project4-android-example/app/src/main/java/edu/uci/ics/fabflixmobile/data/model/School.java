package edu.uci.ics.fabflixmobile.data.model;
import java.io.Serializable;

/**
 * Movie class that captures movie information for movies retrieved from MovieListActivity
 */
public class School implements Serializable{
    private final String name;
    private final String rating;
    private final String location;
    private final String website;
    private final String genre;
    private final String telephone;
    private final String description;


    public School(String name, String rating, String descriptino, String location, String website, String genre, String telephone) {
        this.name = name;
        this.rating = rating;
        this.description = descriptino;
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
    public String getDescription() {return description;}
}