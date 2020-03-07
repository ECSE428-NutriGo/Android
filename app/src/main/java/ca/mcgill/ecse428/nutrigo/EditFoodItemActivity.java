package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class EditFoodItemActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    long food_item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_food_item);
        try {
            wait(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //getIntent().getExtra("SelectedID");
        food_item_id = 1;
        Bundle bundle = getIntent().getExtras();
        food_item_id= bundle.getInt("SelectedID");


               // getIntent().getExtras().getInt("SelectedID");
        // pretend i am passed in the following food item id
        //food_item_id=this.getExtra("");

        //
        RequestParams rp = new RequestParams();
        rp.put("id", food_item_id);


        final EditText name_field = findViewById(R.id.name_field);//not sure if this is gonnaa access edit food item or add food item make sure it works!
        final EditText protein_field = findViewById(R.id.protein_field);
        final EditText carbs_field = findViewById(R.id.carbs_field);
        final EditText fat_field = findViewById(R.id.fat_field);



        asyncHttpClient.addHeader("Authorization", "Token "+LoginActivity.getUserToken());
        asyncHttpClient.put("https://nutrigo-staging.herokuapp.com/nutri/fooditem/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject food_item = (JSONObject) response;
                try {
                    name_field.setText(food_item.get("name").toString());
                    protein_field.setText(food_item.get("protein").toString());
                    carbs_field.setText(food_item.get("carb").toString());
                    fat_field.setText(food_item.get("fat").toString());

                }   catch(JSONException e) {

                }


               // Intent ide = new Intent(EditFoodItemActivity.this, SearchFoodItemActivity.class);//is this just staring the page in aloop again

                //startActivity(ide);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Toast.makeText(EditFoodItemActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public void editFooditem(View view){
        final EditText name_field = findViewById(R.id.name_field);
        final EditText protein_field = findViewById(R.id.protein_field);
        final EditText carbs_field = findViewById(R.id.carbs_field);
        final EditText fat_field = findViewById(R.id.fat_field);

        RequestParams rp = new RequestParams();
        rp.put("id",food_item_id);
        rp.put("name", name_field.getText());
        rp.put("protein", protein_field.getText());
        rp.put("fat", fat_field.getText());
        rp.put("carb", carbs_field.getText());

        asyncHttpClient.addHeader("Authorization", "Token "+LoginActivity.getUserToken());
        asyncHttpClient.put("https://nutrigo-staging.herokuapp.com/nutri/fooditem/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Intent ide = new Intent(EditFoodItemActivity.this, SearchFoodItemActivity.class);
                startActivity(ide);
                //this.finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(EditFoodItemActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
                try {
                    wait(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        //this.finish();
    }

    public void cancelEditFoodItem(View view){
        //this may not be ideal see if there is a better way ??
        this.finish();
        //Intent ide = new Intent(EditFoodItemActivity.this, SearchFoodItemActivity.class);
        //startActivity(ide);

    }
}
