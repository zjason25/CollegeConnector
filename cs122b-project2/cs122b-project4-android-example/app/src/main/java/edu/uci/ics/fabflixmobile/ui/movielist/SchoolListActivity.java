package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.School;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SchoolListActivity extends AppCompatActivity {

    // an activity that generates the movie list
    private final String host = "10.0.2.2";
    //    private final String host = "50.18.38.152";
    private final String port = "8080";
    //    private final String port = "8443";
    private final String domain = "cs122b_project2_login_cart_example_war";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;
    //    private final String baseURL = "https://" + host + ":" + port + "/" + domain;
    private String school = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // locates the layout: R(resource)
        setContentView(R.layout.activity_school_list);
        Intent intent = getIntent();
        if (intent != null) {
            school = intent.getStringExtra("key");
        }
        search(school);
    }
    @SuppressLint("SetTextI18n")
    public void search(String school_name) {
//        message.setText("Trying to search");
        // use the same network queue across our application
        // sends a request through a queue managed by a NetworkManager
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest loginRequest = new StringRequest(
                // a POST method to backend Login server
                Request.Method.GET,
                baseURL + "/api/school_list?school=" + school_name + "&fulltext=true" + "&pagenum=20" + "&whichpage=0" + "&location=null&other=null&order=null&genre=null&autocomplete=null",
                // if login successful:
                response -> {
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
                    try {
                        Toast.makeText(getApplicationContext(), "Searching " + school, Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = new JSONArray(response);

                        final ArrayList<School> schools = new ArrayList<>();
                        for (int i = 1; i < jsonArray.length(); i++) {
                            JSONObject school = jsonArray.getJSONObject(i);
                            String name = school.getString("school_name");
                            String rating = school.getString("school_rating");
                            String city = school.getString("school_city");
                            String state = school.getString("school_state");
                            String location = city + ", " + state;
                            String website = school.getString("link_to_website");
                            String school_genre = school.getString("school_genre");
                            String telephone = school.getString("school_name");
                            schools.add(new School(name, rating, location, website, school_genre, telephone));
                        }

                        showResult(schools);
                    } catch (JSONException e) {
                        Log.d("login.error", "Error parsing JSON response: " + e.getMessage());
                    }
                },
                // error
                error -> {
                    // error
                    Log.d("login.error", error.toString());
                }) {
        };
        // important: queue.add is where the login request is actually sent
        // the loginRequest is added to the queue after we define it
        queue.add(loginRequest);
    }
    public void showResult(ArrayList<School> schools) {
        SchoolListAdapter adapter = new SchoolListAdapter(this, schools);
        ListView listView = findViewById(R.id.list);
        // lets the adapater control the response from the list
        listView.setAdapter(adapter);
//         sets onclick listener:
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            // gets movie using position
//        }
    }
}