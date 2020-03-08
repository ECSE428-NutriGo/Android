package ca.mcgill.ecse428.nutrigo;

import androidx.appcompat.app.AppCompatActivity;

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

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.Header;



public class EditUserActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        final EditText edit_protein_field = findViewById(R.id.edit_protein_field);
        final EditText edit_carb_field = findViewById(R.id.edit_carb_field);
        final EditText edit_fat_field = findViewById(R.id.edit_fat_field);
        final EditText edit_current_weight_field = findViewById(R.id.edit_current_weight_field);
        final EditText edit_target_weight_field = findViewById(R.id.edit_target_weight_field);
        final EditText edit_age_field = findViewById(R.id.edit_age_field);
        final EditText edit_hours_field = findViewById(R.id.edit_hours_field);
        RequestParams rp = new RequestParams();
        asyncHttpClient.addHeader("Authorization", "Token "+LoginActivity.getUserToken());
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/rest-auth/user/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               try {
                   edit_protein_field.setText(response.get("protein_target").toString());
                   edit_carb_field.setText(response.get("carb_target").toString());
                   edit_fat_field.setText(response.get("fat_target").toString());
                   edit_current_weight_field.setText(response.get("current_weight").toString());
                   edit_target_weight_field.setText(response.get("target_weight").toString());
                   edit_age_field.setText(response.get("age").toString());
                   edit_hours_field.setText(response.get("hours_activity").toString());
               } catch (JSONException e) {
                   e.printStackTrace();
               }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(EditUserActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }


    public void edit(View view){
        final EditText edit_protein_field = findViewById(R.id.edit_protein_field);
        final EditText edit_carb_field = findViewById(R.id.edit_carb_field);
        final EditText edit_fat_field = findViewById(R.id.edit_fat_field);
        final EditText edit_current_weight_field = findViewById(R.id.edit_current_weight_field);
        final EditText edit_target_weight_field = findViewById(R.id.edit_target_weight_field);
        final EditText edit_age_field = findViewById(R.id.edit_age_field);
        final EditText edit_hours_field = findViewById(R.id.edit_hours_field);

        //Create Request
        RequestParams rp = new RequestParams();
        rp.put("protein_target",edit_protein_field.getText());
        rp.put("carb_target",edit_carb_field.getText());
        rp.put("fat_target",edit_fat_field.getText());
        rp.put("current_weight", edit_current_weight_field.getText());
        rp.put("target_weight", edit_target_weight_field.getText());
        rp.put("age", edit_age_field.getText());
        rp.put("hours_activity", edit_hours_field.getText());

        asyncHttpClient.addHeader("Authorization", "Token "+LoginActivity.getUserToken());
        asyncHttpClient.put("https://nutrigo-staging.herokuapp.com/rest-auth/user/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Intent ide = new Intent(EditUserActivity.this, MainActivity.class);
                startActivity(ide);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(EditUserActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
}



