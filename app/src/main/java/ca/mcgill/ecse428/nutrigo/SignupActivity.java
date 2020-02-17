package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class SignupActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
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
        setContentView(R.layout.activity_signup);
    }

    public void signup(View view) {
        final EditText username_signup_field = findViewById(R.id.username_signup_field);
        final EditText password_signup_field = findViewById(R.id.password_signup_field);
        final EditText password_confirm_signup_field = findViewById(R.id.password_confirm_signup_field);
        final EditText email_signup_field = findViewById(R.id.email_signup_field);

        RequestParams rp = new RequestParams();
        rp.put("username", username_signup_field.getText());
        rp.put("password1", password_signup_field.getText());
        rp.put("password2", password_confirm_signup_field.getText());
        rp.put("email", email_signup_field.getText());

        asyncHttpClient.post("https://nutrigo-staging.herokuapp.com/rest-auth/registration/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    userToken = response.get("key").toString();
                } catch(JSONException e) {

                }
                Toast.makeText(SignupActivity.this, userToken, Toast.LENGTH_LONG).show();
                Intent ide = new Intent(SignupActivity.this, MainActivity.class);
                ide.putExtra("userToken",userToken);
                LoginActivity.setUserToken(userToken);
                startActivity(ide);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //Toast.makeText(SignupActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
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