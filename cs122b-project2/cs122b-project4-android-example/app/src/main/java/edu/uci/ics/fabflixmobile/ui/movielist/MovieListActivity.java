package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.model.Movie;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity {

    // an activity that generates the movie list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // locates the layout: R(resource)
        setContentView(R.layout.activity_movielist);
        // TODO: this should be retrieved from the backend server
        final ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Terminal", (short) 2004));
        movies.add(new Movie("The Final Season", (short) 2007));
        // shows the items in the movieList using MovieListViewAdapter class
        MovieListViewAdapter adapter = new MovieListViewAdapter(this, movies);
        ListView listView = findViewById(R.id.list);


        // lets the adapater control the response from the list
        listView.setAdapter(adapter);


//         sets onclick listener:
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // gets movie using position
            Movie movie = movies.get(position);
            @SuppressLint("DefaultLocale") String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });
    }
}