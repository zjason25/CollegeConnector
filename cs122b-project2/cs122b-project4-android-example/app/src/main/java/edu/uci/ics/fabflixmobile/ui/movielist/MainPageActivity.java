package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;

import edu.uci.ics.fabflixmobile.ui.login.LoginActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;
import edu.uci.ics.fabflixmobile.data.model.School;
import java.util.ArrayList;


public class MainPageActivity extends AppCompatActivity {

    private EditText search_bar;
    private TextView message;
    private Button searchButton;

    // local
//    private final String host = "10.0.2.2";
//    private final String port = "8080";
//    private final String domain = "cs122b_project2_login_cart_example_war";
//    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    // AWS
    private final String host = "50.18.38.152";

    private final String port = "8443";

    private final String domain = "cs122b-project2-login-cart-example";

    private final String baseURL = "https://" + host + ":" + port + "/" + domain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        search_bar = findViewById(R.id.search_bar);
        message = findViewById(R.id.message);
        searchButton = findViewById(R.id.search);

        searchButton.setOnClickListener(view -> search());
    }

    // go back to previous activity on back button
    @Override
    public void onBackPressed() {
        // Call finish() to close the current activity and return to the previous one
        finish();
        Intent GoBack = new Intent(MainPageActivity.this, LoginActivity.class);
        startActivity(GoBack);
    }

    @SuppressLint("SetTextI18n")
    public void search() {
        message.setText("Trying to search");
        // use the same network queue across our application
        // sends a request through a queue managed by a NetworkManager
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest loginRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/school_list?school=" + search_bar.getText().toString() + "&fulltext=true" + "&pagenum=100" + "&whichpage=0" + "&location=null&other=null&order=null&genre=null&autocomplete=null",
                response -> {
                    try {
                        // returned server response
                        JSONArray jsonArray = new JSONArray(response);
                        // if empty, return
                        if (jsonArray.length() < 1) {
                            message.setText("No matching results");
                            return;
                        }
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
                            String telephone = school.getString("telephone");
                            schools.add(new School(name, rating, location, website, school_genre, telephone));
                        }
                            Toast.makeText(getApplicationContext(), "Search success!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), schools.toArray().length + " results returned", Toast.LENGTH_SHORT).show();

                            // start new activity in school_list
                            finish();
                            Intent SchoolList = new Intent(MainPageActivity.this, SchoolListActivity.class);
                            SchoolList.putExtra("school_list", schools);
                            SchoolList.putExtra("pagenum", 1);
                            startActivity(SchoolList);
                    } catch (JSONException e) {
                        Log.d("login.error", "Error parsing JSON response: " + e.getMessage());
                        message.setText("No matching results");
                    }
                },
                // error
                error -> {
                    Log.d("login.error", error.toString());
                }) {
        };
        queue.add(loginRequest);
    }
}
