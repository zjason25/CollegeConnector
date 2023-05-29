package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.model.School;

import java.util.ArrayList;

public class SchoolListActivity extends AppCompatActivity {
    private int pagenum = 1;
    private final int ITEMS_PER_PAGE = 10;
    private ArrayList<School> schools = new ArrayList<>();
    private Button next;
    private Button previous;
    private TextView pageNumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_list);

        previous = findViewById(R.id.button_previous);
        next = findViewById(R.id.button_next);
        pageNumTextView = findViewById(R.id.page_number);

        previous.setOnClickListener(view -> loadPrevious());
        next.setOnClickListener(view -> loadNext());

        schools = (ArrayList<School>) getIntent().getSerializableExtra("school_list");
        pagenum = getIntent().getIntExtra("pagenum", 1);
        loadPage(pagenum, schools);

    }
    @Override
    public void onBackPressed() {
        // Call finish() to close the current activity and return to the previous one
        finish();
        Intent GoBack = new Intent(SchoolListActivity.this, MainPageActivity.class);
        startActivity(GoBack);
    }
    public void loadPrevious() {
        if (pagenum > 1) {
            pagenum--;
            loadPage(pagenum, schools);
        }
        else {
            Toast.makeText(getApplicationContext(), "You're on first page :)", Toast.LENGTH_SHORT).show();
        }
    }
    public void loadNext() {
        // Increment the current page number based on your pagination logic
        pagenum++;
        loadPage(pagenum, schools);
    }
    private void loadPage(int pageNumber, ArrayList<School> schools) {

        int startIndex = (pageNumber - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, schools.toArray().length);

        if (startIndex >= endIndex) {
            // Handle the case when there are no more pages available
            Toast.makeText(getApplicationContext(), "This is the last page :)", Toast.LENGTH_SHORT).show();
            return;
        }
        pageNumTextView.setText("Page " + pagenum);

        ArrayList<School> pageData = new ArrayList<>(schools.subList(startIndex, endIndex));

        SchoolListAdapter adapter = new SchoolListAdapter(this, pageData);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // gets movie using position
            School school = pageData.get(position);
            @SuppressLint("DefaultLocale") String message = String.format("Clicked on %s", school.getName());
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            finish();
            Intent SchoolList = new Intent(SchoolListActivity.this, SingleSchoolActivity.class);
            SchoolList.putExtra("school", school);
            SchoolList.putExtra("school_list", schools);
            SchoolList.putExtra("pagenum", pagenum);
            // activate the list page.
            // then start the intent
            startActivity(SchoolList);
        });
    }
}