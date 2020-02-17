package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static String getUserToken() {
        return userToken;
    }

    public static void setUserToken(String userToken) {
        LoginActivity.userToken = userToken;
    }

    private static String userToken;

    String error = "";

    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = findViewById(R.id.textView_error);
        tvError.setText(error.trim());

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void navSignup(View view) {
        Intent ide = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(ide);
    }

    public void login(View view) {
        final EditText username_login_field = findViewById(R.id.login_username_field);
        final EditText password_login_field = findViewById(R.id.login_password_field);

        RequestParams rp = new RequestParams();
        rp.put("username", username_login_field.getText());
        rp.put("email", username_login_field.getText());
        rp.put("password", password_login_field.getText());

        asyncHttpClient.post("https://nutrigo-staging.herokuapp.com/rest-auth/login/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    userToken = response.get("key").toString();
                } catch(JSONException e) {

                }
                error = "";
                refreshErrorMessage();
                Intent ide = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(ide);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });
    }
}