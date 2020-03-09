package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ChangeContactInfoActivity extends AppCompatActivity {
    private String oldpass;
    private String username;
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_contact_info);
        final EditText edit_username_field = findViewById(R.id.edit_username_field);

        RequestParams rp = new RequestParams();
        asyncHttpClient.addHeader("Authorization", "Token "+LoginActivity.getUserToken());
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/rest-auth/user/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    edit_username_field.setText(response.get("username").toString());
                    oldpass= response.get("password").toString();
                    username = response.get("username").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText( ChangeContactInfoActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void change(){
        final EditText edit_username_field = findViewById(R.id.edit_username_field);
        final EditText edit_curr_field = findViewById(R.id.edit_current_pass_field);
        final EditText edit_new_pass_field = findViewById(R.id.edit_new_pass_field);
        final EditText edit_new_pass2_field = findViewById(R.id.edit_new_pass2_field);

        if(!edit_new_pass_field.getText().equals(edit_new_pass2_field.getText())){
            return;
        }
        if(!edit_curr_field.getText().equals(oldpass)){
            return;
        }
        if(!edit_username_field.getText().equals(username)){
            return;
        }

        RequestParams rp = new RequestParams();
        rp.put("username",edit_username_field.getText());
        rp.put("password",edit_new_pass_field.getText());
        asyncHttpClient.addHeader("Authorization", "Token "+LoginActivity.getUserToken());
        asyncHttpClient.put("https://nutrigo-staging.herokuapp.com/rest-auth/user/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                oldpass= edit_curr_field.getText().toString();
                Intent ide = new Intent(ChangeContactInfoActivity.this, MainActivity.class);
                startActivity(ide);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText( ChangeContactInfoActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }



}
