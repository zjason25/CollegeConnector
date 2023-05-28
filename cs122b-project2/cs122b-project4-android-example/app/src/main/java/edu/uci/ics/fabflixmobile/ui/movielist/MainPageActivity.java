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

import android.widget.ListView;
import android.widget.Toast;

import edu.uci.ics.fabflixmobile.data.model.School;

import java.util.ArrayList;


public class MainPageActivity extends AppCompatActivity {

    private EditText search_bar;
    private TextView message;
    private Button searchButton;

    /*
      In Android, localhost is the address of the device or the emulator.
      To connect to your machine, you need to use the below IP address
     */
    private final String host = "10.0.2.2";
    //    private final String host = "50.18.38.152";
    private final String port = "8080";
    //    private final String port = "8443";
    private final String domain = "cs122b_project2_login_cart_example_war";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;
//    private final String baseURL = "https://" + host + ":" + port + "/" + domain;

    @Override
    // similar to a main function
    // a bundle: saves instance state in case the app process is killed
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        // upon creation, inflate and initialize the layout
        // gets hold of the layout defined in activity_login.xml
        setContentView(R.layout.activity_search);

        search_bar = findViewById(R.id.search_bar);
        message = findViewById(R.id.message);
        // binds to the button with identifier @+id/button in activity_login.xml
        searchButton = findViewById(R.id.search);
        //assign a listener to call a function to handle the user request when clicking a button
        // calls login() whenever user clicks the button
        searchButton.setOnClickListener(view -> search());
    }

    @SuppressLint("SetTextI18n")
    public void search() {
        message.setText("Trying to search");
        // use the same network queue across our application
        // sends a request through a queue managed by a NetworkManager
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest loginRequest = new StringRequest(
                // a POST method to backend Login server
                Request.Method.GET,
                baseURL + "/api/search?school_name=" + search_bar.getText().toString() + "&fulltext=true" + "&pagenum=20" + "&whichpage=0" + "&location=&other=&order=&genre=&autocomplete=",
                // if login successful:
                response -> {
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        final ArrayList<School> schools = new ArrayList<>();
                        JSONObject school = jsonArray.getJSONObject(1);
                        String name = school.getString("school_name");
                        if (name != null) {
                            Toast.makeText(getApplicationContext(), "Search success!", Toast.LENGTH_SHORT).show();
                            finish();
                            // initialize the activity(page)/destination
                            // start a new Intent(currentActivity, newActivity)
                            Intent SchoolList = new Intent(MainPageActivity.this, SchoolListActivity.class);
                            SchoolList.putExtra("query", search_bar.getText().toString());
                            // activate the list page.
                            // then start the intent
                            startActivity(SchoolList);
                        }
                    } catch (JSONException e) {
                        Log.d("login.error", "Error parsing JSON response: " + e.getMessage());
                        message.setText("No matching results");
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
}
