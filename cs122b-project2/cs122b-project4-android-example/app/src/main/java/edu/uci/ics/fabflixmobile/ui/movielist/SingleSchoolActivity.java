package edu.uci.ics.fabflixmobile.ui.movielist;

import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.model.School;

import java.util.ArrayList;

public class SingleSchoolActivity extends AppCompatActivity {
    // an activity that generates single school
    private ArrayList<School> schools = new ArrayList<>();
    private int pagenum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // locates the layout: R(resource)
        setContentView(R.layout.activity_single_school);
        schools = (ArrayList<School>) getIntent().getSerializableExtra("school_list");
        School school = (School) getIntent().getSerializableExtra("school");
        pagenum = getIntent().getIntExtra("pagenum", 1);

        TextView nameTextView = findViewById(R.id.school_name);
        TextView ratingTextView = findViewById(R.id.school_rating);
        TextView locationTextView = findViewById(R.id.school_location);
        TextView websiteTextView = findViewById(R.id.school_website);
        TextView genreTextView = findViewById(R.id.school_genre);
        TextView telephoneTextView = findViewById(R.id.school_telephone);

        nameTextView.setText(school.getName());
        ratingTextView.setText("Rating: " + school.getRating());
        locationTextView.setText(school.getLocation());
        websiteTextView.setText(school.getWebsite());
        genreTextView.setText(school.getGenre());
        telephoneTextView.setText(school.getTelephone());
    }
    @Override
    public void onBackPressed() {
        // Call finish() to close the current activity and return to the previous one
        finish();
        Intent School_list = new Intent(SingleSchoolActivity.this, SchoolListActivity.class);
        School_list.putExtra("school_list", schools);
        School_list.putExtra("pagenum", pagenum);
        startActivity(School_list);
    }
}