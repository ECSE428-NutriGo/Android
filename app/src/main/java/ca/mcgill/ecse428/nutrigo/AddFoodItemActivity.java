package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;


public class AddFoodItemActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_item);
    }

    public void addFooditem(View view){
        final EditText name_field = findViewById(R.id.name_field);
        final EditText protein_field = findViewById(R.id.protein_field);
        final EditText carbs_field = findViewById(R.id.carbs_field);
        final EditText fat_field = findViewById(R.id.fat_field);

        RequestParams rp = new RequestParams();
        rp.put("name", name_field.getText());
        rp.put("protein", protein_field.getText());
        rp.put("fat", fat_field.getText());
        rp.put("carb", carbs_field.getText());

        asyncHttpClient.addHeader("Authorization", "Token "+LoginActivity.getUserToken());
        asyncHttpClient.post("https://nutrigo-staging.herokuapp.com/nutri/fooditem/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Intent ide = new Intent(AddFoodItemActivity.this, SearchFoodItemActivity.class);
                startActivity(ide);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(AddFoodItemActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

