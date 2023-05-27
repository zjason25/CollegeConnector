package edu.uci.ics.fabflixmobile.ui.login;

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
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.databinding.ActivityLoginBinding;
import edu.uci.ics.fabflixmobile.ui.movielist.MovieListActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView message;

    /*
      In Android, localhost is the address of the device or the emulator.
      To connect to your machine, you need to use the below IP address
     */
    private final String host = "10.0.2.2";
    private final String port = "8080";
    private final String domain = "cs122b_project2_login_cart_example_war";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    @Override
    // similar to a main function
    // a bundle: saves instance state in case the app process is killed
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        // upon creation, inflate and initialize the layout
        // gets hold of the layout defined in activity_login.xml
        setContentView(binding.getRoot());

        username = binding.username;
        password = binding.password;
        message = binding.message;
        // binds to the button with identifier @+id/button in activity_login.xml
        final Button loginButton = binding.login;

        //assign a listener to call a function to handle the user request when clicking a button
        // calls login() whenever user clicks the button
        loginButton.setOnClickListener(view -> login());
    }

    @SuppressLint("SetTextI18n")
    public void login() {
        message.setText("Trying to login");
        // use the same network queue across our application
        // sends a request through a queue managed by a NetworkManager
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest loginRequest = new StringRequest(
                // a POST method to backend Login server
                Request.Method.GET,
                baseURL + "/api/login?username=" + username.getText().toString() + "&password=" + password.getText().toString(),
                // if login successful:
                response -> {
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        if (status.equals("success")) {
                            message.setText("success!");
                            Log.d("login.success", response);
                            //Complete and destroy login activity once successful
                            // finish current login activity
                            finish();
                            // initialize the activity(page)/destination
                            // start a new Intent(currentActivity, newActivity)
                            Intent MovieListPage = new Intent(LoginActivity.this, MovieListActivity.class);
                            // activate the list page.
                            // then start the intent
                            startActivity(MovieListPage);
                        } else {
                            String errorMessage = jsonResponse.getString("message");
                            Log.d("login.error", errorMessage);
                            message.setText(errorMessage);
                        }
                    } catch (JSONException e) {
                        Log.d("login.error", "Error parsing JSON response: " + e.getMessage());
                    }
                },
                // error
                error -> {
                    // error
                    Log.d("login.error", error.toString());
                }) {
            @Override
            // override getParams function to parse values from response
            // add attribute "username" and "password"
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };
        // important: queue.add is where the login request is actually sent
        // the loginRequest is added to the queue after we define it
        queue.add(loginRequest);
    }
}