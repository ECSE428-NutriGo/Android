package ca.mcgill.ecse428.nutrigo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignupActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void signup(View view) {
        final EditText username_signup_field = findViewById(R.id.username_signup_field);
        final EditText password_signup_field = findViewById(R.id.password_signup_field);

        RequestParams rp = new RequestParams();
        rp.put("username", username_signup_field.getText());
        rp.put("password", password_signup_field.getText());

        asyncHttpClient.put("https://nutrigo-staging.herokuapp.com/rest-auth/user/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                final TextView signup_title = findViewById(R.id.signup_title);
                signup_title.setText("Congrats, you signed up");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }
}