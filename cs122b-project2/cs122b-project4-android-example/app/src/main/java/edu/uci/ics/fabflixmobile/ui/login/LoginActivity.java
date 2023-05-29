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
import edu.uci.ics.fabflixmobile.ui.movielist.MainPageActivity;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private TextView message;
    /*
      IP Address
     */

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
        final StringRequest loginRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/login?username=" + username.getText().toString() + "&password=" + password.getText().toString(),
                // if login successful:
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        if (status.equals("success")) {
                            message.setText("success!");
                            Log.d("login.success", response);
                            //Complete and destroy login activity once successful
                            finish();
                            // initialize the activity(page)/destination
                            Intent MainPage = new Intent(LoginActivity.this, MainPageActivity.class);
                            // activate MainPage.
                            startActivity(MainPage);
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
                    Log.d("login.error", error.toString());
                }) {
        };
        // important: queue.add is where the login request is actually sent
        // the loginRequest is added to the queue after we define it
        queue.add(loginRequest);
    }
}