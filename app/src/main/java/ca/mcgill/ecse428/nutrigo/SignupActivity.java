package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
                String error = errorResponse.toString();
                String[] messages = error.split("],\"");
                String message = "";
                for(int i = 0; i < messages.length; i++) {
                    int a = messages[i].indexOf("[");
                    if(i == messages.length - 1) {
                        message += messages[i].substring(a+2, messages[i].length()-3);
                    }
                    else{
                        message += messages[i].substring(a+2, messages[i].length()-1)+"\n";
                    }
                }
                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}