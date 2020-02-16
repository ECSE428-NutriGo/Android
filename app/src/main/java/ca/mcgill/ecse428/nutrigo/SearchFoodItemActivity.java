package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class SearchFoodItemActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    Intent intent;
    String error;
    ArrayList<String> foodItemNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchfooditem);

        ListView list = findViewById(R.id.listThing);
        final ArrayAdapter adapter = new ArrayAdapter(SearchFoodItemActivity.this, android.R.layout.simple_list_item_1, foodItemNames);
        list.setAdapter(adapter);

        intent = new Intent(this, AddMealActivity.class);

        asyncHttpClient.addHeader("Authorization", "Token adf5ca6fa7ad08d8cb1fdfd471a92a92d6442997");
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/nutri/fooditem/", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            foodItemNames.add(response.get("fooditems").toString());
                        } catch (Exception e) {
                            error += e.getMessage();
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                try {
//                    error += errorResponse.get("message").toString();
//                } catch (JSONException e) {
//                    error += e.getMessage();
//                }
//                refreshErrorMessage();
            }

        });
    }

        //mealNameBox = findViewById(R.id.editText_mealname);

    public void backToAddMeal(View view){
        startActivity(intent);
    }

    public void addFoodItem(View view){

    }

    public void createNewFoodItem(View view){
//        intent = new Intent(this, CreateNewFoodItemActivity.class);
//        startActivity(intent);
    }
}
