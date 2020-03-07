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


import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.Header;



public class EditUserActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
    }


    public void edit(View view){
        final EditText edit_age_field = findViewById(R.id.edit_age_field);
        final EditText edit_current_weight_field = findViewById(R.id.edit_current_weight_field);
        final EditText edit_target_weight_field = findViewById(R.id.edit_target_weight_field);


        RequestParams rp = new RequestParams();
        rp.put("age", edit_age_field.getText());
        rp.put("currentweight", edit_current_weight_field.getText());
        rp.put("targetweight", edit_target_weight_field.getText());

        //age

        //Edit Target weight
        //Current Weight

        asyncHttpClient.addHeader("Authorization", "Token "+LoginActivity.getUserToken());
        asyncHttpClient.put("https://nutrigo-staging.herokuapp.com/rest-auth/user/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Intent ide = new Intent(EditUserActivity.this, SearchFoodItemActivity.class);
                startActivity(ide);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(EditUserActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
}



